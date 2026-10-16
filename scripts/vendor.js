// Copia o leitor de texto (Tesseract) e os idiomas para dentro do app, para funcionar sem internet.
const fs = require('fs'), path = require('path');
const nm = p => path.join(__dirname, '..', 'node_modules', p);
const out = path.join(__dirname, '..', 'www', 'vendor');
fs.mkdirSync(path.join(out, 'core'), { recursive: true });
fs.mkdirSync(path.join(out, 'lang'), { recursive: true });
const cp = (from, to) => { fs.copyFileSync(nm(from), path.join(out, to)); console.log('ok', to); };
cp('tesseract.js/dist/tesseract.min.js', 'tesseract.min.js');
cp('tesseract.js/dist/worker.min.js', 'worker.min.js');
for (const f of ['tesseract-core-lstm.wasm.js', 'tesseract-core-simd-lstm.wasm.js'])
  cp('tesseract.js-core/' + f, 'core/' + f);
for (const l of ['eng', 'por'])
  cp(`@tesseract.js-data/${l}/4.0.0_best_int/${l}.traineddata.gz`, `lang/${l}.traineddata.gz`);
