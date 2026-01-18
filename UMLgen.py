#!/usr/bin/env python3
"""
Generate a basic PlantUML class diagram from a Java project, INCLUDING packages.

Usage:
  python java2puml.py /path/to/java/project -o diagram.puml

Render locally (recommended):
  plantuml -tsvg diagram.puml
  # then convert SVG -> PDF (Inkscape / browser print)
"""

from __future__ import annotations
import argparse
import os
import re
from dataclasses import dataclass, field
from typing import Dict, List, Optional, Set, Tuple

JAVA_FILE_RE = re.compile(r".*\.java$", re.IGNORECASE)

PKG_RE = re.compile(r"^\s*package\s+([\w\.]+)\s*;", re.MULTILINE)
TYPE_DECL_RE = re.compile(
    r"(?P<prefix>^\s*(?:public|protected|private|abstract|final|static|\s)*)"
    r"(?P<kind>class|interface|enum)\s+"
    r"(?P<name>[A-Za-z_]\w*)"
    r"(?:\s+extends\s+(?P<extends>[A-Za-z_]\w*(?:\.[A-Za-z_]\w*)*))?"
    r"(?:\s+implements\s+(?P<implements>[A-Za-z_]\w*(?:\.[A-Za-z_]\w*)*(?:\s*,\s*[A-Za-z_]\w*(?:\.[A-Za-z_]\w*)*)*))?"
    r"\s*\{",
    re.MULTILINE,
)

# crude field + method matchers, ignore lots of Java syntax details intentionally
FIELD_RE = re.compile(
    r"^\s*(?P<mods>(?:public|protected|private|static|final|transient|volatile|\s)+)\s*"
    r"(?P<type>[A-Za-z_]\w*(?:<[^;>]+>)?(?:\[\])*)\s+"
    r"(?P<name>[A-Za-z_]\w*)\s*(?:=\s*[^;]+)?\s*;",
    re.MULTILINE,
)

METHOD_RE = re.compile(
    r"^\s*(?P<mods>(?:public|protected|private|static|final|abstract|synchronized|native|\s)+)\s*"
    r"(?P<ret>[A-Za-z_]\w*(?:<[^)>]+>)?(?:\[\])*)\s+"
    r"(?P<name>[A-Za-z_]\w*)\s*"
    r"\((?P<params>[^)]*)\)\s*(?:throws\s+[^{]+)?\s*\{?",
    re.MULTILINE,
)

CTOR_RE = re.compile(
    r"^\s*(?P<mods>(?:public|protected|private|\s)+)\s*"
    r"(?P<name>[A-Za-z_]\w*)\s*"
    r"\((?P<params>[^)]*)\)\s*(?:throws\s+[^{]+)?\s*\{?",
    re.MULTILINE,
)

COMMENT_BLOCK_RE = re.compile(r"/\*.*?\*/", re.DOTALL)
COMMENT_LINE_RE = re.compile(r"//.*?$", re.MULTILINE)

PRIMITIVES = {
    "byte", "short", "int", "long", "float", "double", "boolean", "char", "void",
    "String", "Integer", "Long", "Double", "Float", "Boolean", "Character", "Object",
}

def strip_comments(text: str) -> str:
    text = COMMENT_BLOCK_RE.sub("", text)
    text = COMMENT_LINE_RE.sub("", text)
    return text

def vis_symbol(mods: str) -> str:
    mods = mods or ""
    if "private" in mods:
        return "-"
    if "protected" in mods:
        return "#"
    if "public" in mods:
        return "+"
    return "~"  # package-private

def clean_type(t: str) -> str:
    t = t.strip()
    # remove generics: List<Foo> -> List
    t = re.sub(r"<.*?>", "", t)
    return t

def simple_name(qualified: str) -> str:
    return qualified.split(".")[-1].strip()

def parse_params(params: str) -> List[Tuple[str, str]]:
    params = params.strip()
    if not params:
        return []
    out: List[Tuple[str, str]] = []
    # naive split by comma (won't handle generics with commas perfectly)
    for p in [x.strip() for x in params.split(",") if x.strip()]:
        # remove annotations
        p = re.sub(r"@\w+(?:\([^)]*\))?\s*", "", p).strip()
        parts = p.split()
        if len(parts) >= 2:
            ptype = clean_type(parts[-2])
            pname = parts[-1].strip()
            out.append((ptype, pname))
        else:
            out.append((clean_type(parts[0]), "arg"))
    return out

@dataclass
class JavaType:
    kind: str  # class/interface/enum
    name: str
    package: str = ""
    extends: Optional[str] = None              # may be qualified or simple
    implements: List[str] = field(default_factory=list)  # may be qualified or simple
    fields: List[Tuple[str, str, str]] = field(default_factory=list)  # (vis, type, name)
    methods: List[Tuple[str, str, str, List[Tuple[str, str]]]] = field(default_factory=list)  # (vis, ret, name, params)
    ctors: List[Tuple[str, str, List[Tuple[str, str]]]] = field(default_factory=list)  # (vis, name, params)

    def fq(self) -> str:
        return f"{self.package}.{self.name}" if self.package else self.name

def find_java_files(root: str) -> List[str]:
    hits: List[str] = []
    for base, _, files in os.walk(root):
        for f in files:
            if JAVA_FILE_RE.match(f):
                hits.append(os.path.join(base, f))
    return hits

def parse_file(path: str) -> List[JavaType]:
    with open(path, "r", encoding="utf-8", errors="ignore") as fp:
        raw = fp.read()

    text = strip_comments(raw)

    pkg = ""
    m = PKG_RE.search(text)
    if m:
        pkg = m.group(1)

    types: List[JavaType] = []
    for tm in TYPE_DECL_RE.finditer(text):
        kind = tm.group("kind")
        name = tm.group("name")
        ext = tm.group("extends")
        impl = tm.group("implements")
        implements: List[str] = []
        if impl:
            implements = [x.strip() for x in impl.split(",") if x.strip()]
        jt = JavaType(
            kind=kind,
            name=name,
            package=pkg,
            extends=ext.strip() if ext else None,
            implements=implements,
        )
        types.append(jt)

    if not types:
        return []

    # If multiple classes per file, we still do a crude global scan for members and attach to first type
    target = types[0]

    for fm in FIELD_RE.finditer(text):
        mods = fm.group("mods") or ""
        v = vis_symbol(mods)
        t = clean_type(fm.group("type"))
        n = fm.group("name")
        target.fields.append((v, t, n))

    for mm in METHOD_RE.finditer(text):
        mods = mm.group("mods") or ""
        v = vis_symbol(mods)
        ret = clean_type(mm.group("ret"))
        name = mm.group("name")
        params = parse_params(mm.group("params") or "")
        if name == target.name:  # constructor
            continue
        target.methods.append((v, ret, name, params))

    for cm in CTOR_RE.finditer(text):
        mods = cm.group("mods") or ""
        v = vis_symbol(mods)
        name = cm.group("name")
        if name != target.name:
            continue
        params = parse_params(cm.group("params") or "")
        target.ctors.append((v, name, params))

    return types

def generate_puml(types: Dict[str, JavaType]) -> str:
    lines: List[str] = []
    lines.append("@startuml")
    lines.append("skinparam classAttributeIconSize 0")
    lines.append("hide empty members")

    # Build name indexes
    fq_by_simple: Dict[str, Set[str]] = {}
    for fq, t in types.items():
        fq_by_simple.setdefault(t.name, set()).add(fq)

    def resolve_to_fq(type_ref: Optional[str], ctx_pkg: str) -> Optional[str]:
        """Resolve possibly-qualified type name to an fq key in `types`."""
        if not type_ref:
            return None
        ref = type_ref.strip()
        # If qualified, try exact match
        if "." in ref:
            return ref if ref in types else simple_name(ref) if simple_name(ref) in types else None
        # Simple name: prefer same package if exists, else if unique globally use it
        cand_same_pkg = f"{ctx_pkg}.{ref}" if ctx_pkg else ref
        if cand_same_pkg in types:
            return cand_same_pkg
        cands = list(fq_by_simple.get(ref, []))
        if len(cands) == 1:
            return cands[0]
        return None  # ambiguous or unknown

    def puml_id(fq: str) -> str:
        """Stable PlantUML identifier safe for dots."""
        return "T_" + re.sub(r"[^A-Za-z0-9_]", "_", fq)

    # Group by package
    pkgs: Dict[str, List[str]] = {}
    for fq, t in types.items():
        pkgs.setdefault(t.package or "(default)", []).append(fq)
    for pkg in pkgs:
        pkgs[pkg].sort()

    # Declare types inside package blocks
    for pkg in sorted(pkgs.keys()):
        if pkg == "(default)":
            lines.append('package "(default)" {')
        else:
            lines.append(f"package {pkg} {{")

        for fq in pkgs[pkg]:
            t = types[fq]
            pid = puml_id(fq)

            if t.kind == "interface":
                header = f'interface "{t.name}" as {pid}'
            elif t.kind == "enum":
                header = f'enum "{t.name}" as {pid}'
            else:
                header = f'class "{t.name}" as {pid}'

            lines.append(header + " {")

            for v, ty, n in t.fields:
                # display simple type, keep arrays
                disp_ty = simple_name(ty.replace("[]", "")) + ("[]" if ty.strip().endswith("[]") else "")
                lines.append(f"  {v} {n} : {disp_ty}")

            for v, name, params in t.ctors:
                p = ", ".join([f"{pn} : {simple_name(pt)}" for pt, pn in params])
                lines.append(f"  {v} {name}({p})")

            for v, ret, name, params in t.methods:
                p = ", ".join([f"{pn} : {simple_name(pt)}" for pt, pn in params])
                lines.append(f"  {v} {name}({p}) : {simple_name(ret)}")

            lines.append("}")

        lines.append("}")

    # Relationships: extends / implements
    for fq, t in types.items():
        child = puml_id(fq)

        parent_fq = resolve_to_fq(t.extends, t.package)
        if parent_fq:
            lines.append(f"{puml_id(parent_fq)} <|-- {child}")

        for iface in t.implements:
            iface_fq = resolve_to_fq(iface, t.package)
            if iface_fq:
                lines.append(f"{puml_id(iface_fq)} <|.. {child}")

    # Associations from field types
    for fq, t in types.items():
        src = puml_id(fq)
        for _v, ty, _fname in t.fields:
            base = ty.strip()
            is_array = base.endswith("[]")
            base = base[:-2] if is_array else base
            base = clean_type(base)
            base_simple = simple_name(base)

            if base_simple in PRIMITIVES:
                continue

            dst_fq = resolve_to_fq(base, t.package)
            if dst_fq and dst_fq != fq:
                lines.append(f"{src} --> {puml_id(dst_fq)}")

    lines.append("@enduml")
    return "\n".join(lines)

def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("root", help="Root folder of the Java project")
    ap.add_argument("-o", "--out", default="diagram.puml", help="Output .puml path")
    args = ap.parse_args()

    java_files = find_java_files(args.root)
    types: Dict[str, JavaType] = {}

    for f in java_files:
        for t in parse_file(f):
            k = t.fq()  # fully-qualified key to avoid collisions
            if k not in types:
                types[k] = t

    puml = generate_puml(types)
    with open(args.out, "w", encoding="utf-8") as fp:
        fp.write(puml)

    print(f"Wrote {args.out} with {len(types)} types from {len(java_files)} Java files.")

if __name__ == "__main__":
    main()
