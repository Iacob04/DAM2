// Apuntes PSP: resaltado de Java, botón Copiar, buscador, filtros, índice activo y tema.

document.addEventListener("DOMContentLoaded", function () {

  // ---------- 0. Tema claro / oscuro ----------
  const raiz = document.documentElement;
  const botonTema = document.getElementById("tema");
  try {
    const guardado = localStorage.getItem("psp-tema");
    if (guardado) raiz.dataset.theme = guardado;
  } catch (e) { /* sin almacenamiento: tema del sistema */ }
  if (botonTema) {
    botonTema.addEventListener("click", function () {
      const oscuro = raiz.dataset.theme
        ? raiz.dataset.theme === "dark"
        : window.matchMedia("(prefers-color-scheme: dark)").matches;
      raiz.dataset.theme = oscuro ? "light" : "dark";
      try { localStorage.setItem("psp-tema", raiz.dataset.theme); } catch (e) {}
    });
  }

  // ---------- 1. Resaltado de sintaxis Java ----------
  const reservadas = new Set([
    "public", "private", "protected", "static", "void", "class", "new", "return",
    "if", "else", "while", "do", "for", "try", "catch", "finally", "throws", "throw", "import",
    "package", "int", "long", "double", "float", "char", "boolean", "break", "continue", "case",
    "switch", "default", "final", "null", "true", "false", "module", "extends", "implements",
    "interface", "this", "super", "synchronized", "volatile", "instanceof", "var"
  ]);

  function escapar(t) {
    return t.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
  }

  // comentario | texto | carácter | número | palabra
  const patron = /(\/\/[^\n]*|\/\*[\s\S]*?\*\/)|("(?:\\.|[^"\\\n])*")|('(?:\\.|[^'\\\n])')|(\b\d[\d_]*(?:\.\d+)?\b)|([A-Za-z_]\w*)/g;

  function resaltarJava(codigo) {
    let html = "";
    let ultimo = 0;
    codigo.replace(patron, function (m, com, str, chr, num, pal, pos) {
      html += escapar(codigo.slice(ultimo, pos));
      if (com) html += '<span class="c">' + escapar(com) + "</span>";
      else if (str || chr) html += '<span class="s">' + escapar(str || chr) + "</span>";
      else if (num) html += '<span class="n">' + num + "</span>";
      else if (reservadas.has(pal)) html += '<span class="k">' + pal + "</span>";
      else if (/^[A-Z]/.test(pal)) html += '<span class="t">' + pal + "</span>";
      else html += pal;
      ultimo = pos + m.length;
    });
    return html + escapar(codigo.slice(ultimo));
  }

  // ---------- 2. Marco de cada bloque: nombre de archivo + Copiar ----------
  document.querySelectorAll("pre").forEach(function (pre) {
    const codigo = pre.textContent;
    const esTexto = pre.dataset.lang === "txt";
    if (!esTexto) pre.innerHTML = resaltarJava(codigo);

    const bloque = document.createElement("div");
    bloque.className = "bloque-codigo" + (esTexto ? " salida" : "");
    pre.parentNode.insertBefore(bloque, pre);

    if (pre.dataset.archivo) {
      const cab = document.createElement("div");
      cab.className = "archivo";
      cab.textContent = pre.dataset.archivo;
      bloque.appendChild(cab);
    }
    bloque.appendChild(pre);

    const boton = document.createElement("button");
    boton.className = "copiar";
    boton.type = "button";
    boton.textContent = "Copiar";
    boton.addEventListener("click", function () {
      const texto = pre.textContent;
      function hecho() {
        boton.textContent = "Copiado";
        setTimeout(function () { boton.textContent = "Copiar"; }, 1500);
      }
      if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(texto).then(hecho, function () { copiarViejo(texto); hecho(); });
      } else {
        copiarViejo(texto);
        hecho();
      }
    });
    bloque.appendChild(boton);
  });

  // Copia para file:// o navegadores sin clipboard API
  function copiarViejo(texto) {
    const area = document.createElement("textarea");
    area.value = texto;
    area.style.position = "fixed";
    area.style.opacity = "0";
    document.body.appendChild(area);
    area.select();
    try { document.execCommand("copy"); } catch (e) {}
    area.remove();
  }

  // ---------- 3. Buscador + filtros ----------
  const input = document.getElementById("busqueda");
  const contador = document.getElementById("contador");
  const secciones = Array.from(document.querySelectorAll("section.tema"));
  const titulosBloque = Array.from(document.querySelectorAll(".bloque-titulo"));
  const portada = document.querySelector(".portada");
  const vacio = document.getElementById("sin-resultados");
  const enlaces = Array.from(document.querySelectorAll(".indice a"));
  const botonesFiltro = Array.from(document.querySelectorAll(".filtros button"));
  let filtro = "todo";

  // Quita tildes letra a letra (mantiene la longitud para poder resaltar)
  function base(t) {
    let r = "";
    for (const ch of t.toLowerCase()) {
      const n = ch.normalize("NFD")[0];
      r += n.length === 1 ? n : ch;
    }
    return r;
  }

  // Texto de cada sección precalculado (sin tildes)
  const textoSeccion = new Map();
  secciones.forEach(function (s) { textoSeccion.set(s, base(s.textContent)); });

  function limpiar(zona) {
    zona.querySelectorAll("mark").forEach(function (m) {
      m.replaceWith(document.createTextNode(m.textContent));
    });
    zona.normalize();
  }

  function marcar(nodo, texto) {
    if (!nodo) return;
    if (nodo.nodeType === Node.TEXT_NODE) {
      const pos = base(nodo.textContent).indexOf(texto);
      if (pos === -1) return;
      const trozo = nodo.splitText(pos);
      trozo.splitText(texto.length);
      const mark = document.createElement("mark");
      mark.textContent = trozo.textContent;
      trozo.replaceWith(mark);
      marcar(mark.nextSibling, texto);
    } else if (nodo.nodeType === Node.ELEMENT_NODE && !["BUTTON", "MARK", "svg", "SVG", "SUMMARY"].includes(nodo.tagName)) {
      Array.from(nodo.childNodes).forEach(function (h) { marcar(h, texto); });
    }
  }

  function aplicar() {
    const bruto = input.value.trim();
    const palabras = base(bruto).split(/\s+/).filter(Boolean);
    let visibles = 0;

    secciones.forEach(function (s) {
      limpiar(s);
      const t = textoSeccion.get(s);
      const pasaFiltro = filtro === "todo" || s.dataset.cat === filtro;
      const pasaTexto = palabras.every(function (p) { return t.includes(p); });
      const ver = pasaFiltro && pasaTexto;
      s.classList.toggle("oculto", !ver);
      if (ver) {
        visibles++;
        palabras.forEach(function (p) { marcar(s, p); });
        // Si la coincidencia está dentro de un <details> cerrado, se abre
        if (palabras.length) s.querySelectorAll("details").forEach(function (d) {
          if (d.querySelector("mark")) d.open = true;
        });
      }
    });

    // Índice lateral: solo los enlaces de secciones visibles
    enlaces.forEach(function (a) {
      const s = document.getElementById(a.getAttribute("href").slice(1));
      if (s) a.parentElement.classList.toggle("oculto", s.classList.contains("oculto"));
    });

    const filtrando = palabras.length > 0 || filtro !== "todo";
    portada.classList.toggle("oculto", filtrando);
    titulosBloque.forEach(function (t) { t.classList.toggle("oculto", filtrando); });
    vacio.classList.toggle("oculto", !(filtrando && visibles === 0));
    contador.textContent = !filtrando ? "" :
      visibles + (visibles === 1 ? " sección" : " secciones");
  }

  input.addEventListener("input", aplicar);

  input.addEventListener("keydown", function (e) {
    if (e.key === "Enter") {
      const primera = secciones.find(function (s) { return !s.classList.contains("oculto"); });
      if (primera) {
        const m = primera.querySelector("mark") || primera;
        m.scrollIntoView({ block: "center" });
      }
    }
    if (e.key === "Escape") {
      input.value = "";
      aplicar();
    }
  });

  botonesFiltro.forEach(function (b) {
    b.addEventListener("click", function () {
      filtro = b.dataset.filtro;
      botonesFiltro.forEach(function (o) { o.setAttribute("aria-pressed", String(o === b)); });
      aplicar();
      window.scrollTo(0, 0);
    });
  });

  // Tecla "/" para saltar al buscador
  document.addEventListener("keydown", function (e) {
    if (e.key === "/" && document.activeElement !== input) {
      e.preventDefault();
      input.focus();
      input.select();
    }
  });

  // ---------- 4. Enlace activo del índice ----------
  const porId = new Map(enlaces.map(function (a) { return [a.getAttribute("href").slice(1), a]; }));
  if ("IntersectionObserver" in window) {
    const obs = new IntersectionObserver(function (entradas) {
      entradas.forEach(function (en) {
        if (en.isIntersecting) {
          enlaces.forEach(function (a) { a.classList.remove("activo"); });
          const a = porId.get(en.target.id);
          if (a) {
            a.classList.add("activo");
            const det = a.closest("details");
            if (det && !det.open) det.open = true;
          }
        }
      });
    }, { rootMargin: "-10% 0px -80% 0px" });
    secciones.forEach(function (s) { obs.observe(s); });
  }

  // ---------- 5. Índice en móvil ----------
  const lateral = document.querySelector(".lateral");
  const botonIndice = document.getElementById("ver-indice");
  if (botonIndice) {
    botonIndice.addEventListener("click", function () {
      const abierto = lateral.classList.toggle("abierto");
      botonIndice.setAttribute("aria-expanded", String(abierto));
    });
    enlaces.forEach(function (a) {
      a.addEventListener("click", function () { lateral.classList.remove("abierto"); });
    });
  }
});
