package br.com.emichelsx.springbootwithdockerapp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/volume")
public class VolumeController {

    @Value("${docker.volume.file.path}")
    private String fileVolumePath;

    @RequestMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity getDadosVolume() throws IOException {
        File file = new File(fileVolumePath);
        if(file.exists()) {
            FileInputStream stream = new FileInputStream(fileVolumePath);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String linha = br.readLine();
            while (linha != null) {
                stringBuilder.append(linha);
                linha = br.readLine();
            }
            return ResponseEntity.ok(stringBuilder.toString());
        }
        return ResponseEntity.ok(String.format("Não foi localizado o arquivo '%s'", fileVolumePath));
    }

}
