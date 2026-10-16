# Leitor de Cartas MTG

App Android que lê o nome de cartas de Magic: The Gathering (inglês e português) pela câmera e monta a lista com quantidades:

```
20 Plains
3 Llanowar Elves
1 Raio
```

## Baixar o APK
Abra a aba **Releases** do repositório no celular, baixe o `.apk` mais recente e instale (o Android pede para permitir "instalar apps desconhecidos").
Cada envio para a branch `main` gera um APK novo automaticamente (aba **Actions**).

## Como usar
- **Contínuo:** encaixe a carta na moldura com o nome na faixa dourada. Ao trocar a carta, ele lê e soma sozinho.
- **Toque:** toque na imagem para ler a carta atual.
- Corrija nomes na lista, ajuste com − e +, e use **Copiar lista**, **Salvar .txt** (vai para Documentos) ou **Enviar**.
- O reconhecimento roda no próprio celular e funciona sem internet.

## Estrutura
- `www/index.html`: o app inteiro (interface, câmera e leitura).
- `scripts/vendor.js`: copia o Tesseract e os idiomas para `www/vendor`.
- `android/`: projeto Android (Capacitor).
- `.github/workflows/build-apk.yml`: compila e publica o APK.
