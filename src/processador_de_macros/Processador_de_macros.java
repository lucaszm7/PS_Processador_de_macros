package processador_de_macros;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Processador_de_macros {

    public static File run(String diretorio){
        
        File arquivo_entrada = new File(diretorio);
        File arquivo_saida = new File("arquivo_saida.asm");
        List<String> conteudo = new ArrayList<>();
        
        try {
            arquivo_saida.delete();
            arquivo_saida.createNewFile();
            Scanner reader = new Scanner(arquivo_entrada);
            
            FileWriter fileWriter = new FileWriter(arquivo_saida);
            BufferedWriter escrever = new BufferedWriter(fileWriter);
            escrever.write("OLA MUNDO ESCREVBENDO");
            
            String line;
            while(reader.hasNextLine()) {
                line = reader.nextLine();
                conteudo.add(line);
            }
            
            escrever.close();
            fileWriter.close();
            
        }
        catch(IOException ex){
            System.out.println(ex);
        }
        
        /*conteudo.set(0, "OLA MUNDO");
        
        for (String iterator : conteudo) {
            System.out.println(iterator);
        }*/
        
        return arquivo_saida;
    } 
    
}
