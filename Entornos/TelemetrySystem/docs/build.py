import subprocess, html, os
from pygments import highlight
from pygments.lexers import JavaLexer, TextLexer
from pygments.formatters import HtmlFormatter
base='../src/'
items=[
 ('TelemetryDiagnosticControls.java (clase refactorizada)', base+'main/java/tddmicroexercises/telemetrysystem/TelemetryDiagnosticControls.java', JavaLexer()),
 ('TelemetryDiagnosticControlsTest.java (tests con Mockito)', base+'test/java/tddmicroexercises/telemetrysystem/TelemetryDiagnosticControlsTest.java', JavaLexer()),
 ('Resultado de mvn test', 'mvn_output.txt', TextLexer()),
]
chrome=r'C:\Program Files\Google\Chrome\Application\chrome.exe'
fmt=HtmlFormatter(style='monokai',linenos='table',noclasses=False)
png=[]
for i,(title,path,lx) in enumerate(items):
    code=open(path,encoding='utf-8').read()
    n=code.count('\n')+1
    body=highlight(code,lx,fmt)
    page=f"""<html><meta charset=utf-8><style>{fmt.get_style_defs('.highlight')}
body{{margin:0;background:#272822;font-family:Consolas,monospace}}
h1{{color:#fff;font:600 16px Segoe UI;background:#3e3d32;margin:0;padding:10px 16px}}
.highlight{{background:#272822;font-size:14px;line-height:1.4;padding:8px 16px}}
.highlight pre{{margin:0}} td.linenos{{padding-right:14px;color:#888}}</style>
<h1>{html.escape(title)}</h1>{body}</html>"""
    h=f'p{i}.html'; open(h,'w',encoding='utf-8').write(page)
    out=os.path.abspath(f'shot{i}.png')
    subprocess.run([chrome,'--headless=new','--disable-gpu','--hide-scrollbars',f'--screenshot={out}',f'--window-size=1100,{int(n*20.5)+80}',f'--force-device-scale-factor=1.5','file:///'+os.path.abspath(h).replace(chr(92),'/')],check=True)
    png.append(out)
from PIL import Image, ImageChops
from reportlab.pdfgen import canvas
from reportlab.lib.pagesizes import A4
W,H=A4
c=canvas.Canvas('Capturas_TelemetrySystem.pdf',pagesize=A4)
bg=(0x27,0x28,0x22)
for p,(t,_,_) in zip(png,items):
    im=Image.open(p).convert('RGB')
    diff=ImageChops.difference(im,Image.new('RGB',im.size,bg))
    im=im.crop((0,0,im.width,diff.getbbox()[3]+20))
    iw=im.width; s=(W-40)/iw
    chunk=int((H-80)/s)
    y=0;part=1
    while y<im.height:
        piece=im.crop((0,y,iw,min(y+chunk,im.height)))
        f=f'piece{part}.png'; piece.save(f)
        c.setFont('Helvetica-Bold',12); c.drawString(20,H-30,t+(f' (parte {part})' if im.height>chunk else ''))
        c.drawImage(f,20,H-45-piece.height*s,iw*s,piece.height*s)
        c.showPage(); y+=chunk; part+=1
c.save()
