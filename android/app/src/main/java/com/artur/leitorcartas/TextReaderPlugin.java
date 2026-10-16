package com.artur.leitorcartas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Base64;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

/** Reconhecimento de texto nativo (Google ML Kit, modelo embutido, funciona sem internet). */
@CapacitorPlugin(name = "TextReader")
public class TextReaderPlugin extends Plugin {
    private TextRecognizer recognizer;

    @Override
    public void load() {
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    @PluginMethod
    public void recognize(PluginCall call) {
        String data = call.getString("image");
        if (data == null || data.isEmpty()) {
            call.reject("Imagem vazia");
            return;
        }
        int comma = data.indexOf(',');
        if (comma >= 0) data = data.substring(comma + 1);
        Bitmap bmp;
        try {
            byte[] bytes = Base64.decode(data, Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            call.reject("Imagem inválida");
            return;
        }
        if (bmp == null) {
            call.reject("Imagem inválida");
            return;
        }
        InputImage image = InputImage.fromBitmap(bmp, 0);
        recognizer.process(image)
            .addOnSuccessListener(result -> {
                JSArray lines = new JSArray();
                for (Text.TextBlock block : result.getTextBlocks()) {
                    for (Text.Line line : block.getLines()) {
                        JSObject o = new JSObject();
                        o.put("text", line.getText());
                        Rect r = line.getBoundingBox();
                        if (r != null) {
                            o.put("x", r.left);
                            o.put("y", r.top);
                            o.put("w", r.width());
                            o.put("h", r.height());
                        }
                        lines.put(o);
                    }
                }
                JSObject ret = new JSObject();
                ret.put("text", result.getText());
                ret.put("lines", lines);
                call.resolve(ret);
            })
            .addOnFailureListener(e -> call.reject(e.getMessage() == null ? "Falha na leitura" : e.getMessage()));
    }
}
