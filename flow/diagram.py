import sys, re, subprocess

dot_template = '''digraph flow {
  rankdir=LR
  node [shape=box]
%s}'''
edge_template = '  %s -> %s;\n'

def create_dot(edges):
    edges_str = ''
    for edge in edges:
        edges_str += edge_template % edge
    with open('flow.dot', 'w') as f:
        f.write(dot_template % edges_str)

with open(sys.argv[1]) as f:
    lines = f.readlines()

edges = []
for l in lines:
    m = re.match(r'\s*connect\(\s*(\w+),\s*(\w+)\s*\)\s*;\s*\n', l)
    if m:
        src = m.group(1)
        dest = m.group(2)
        edges.append((src, dest))

create_dot(edges)
subprocess.call(['dot', '-Tpng', '-oflow.png', 'flow.dot'])

