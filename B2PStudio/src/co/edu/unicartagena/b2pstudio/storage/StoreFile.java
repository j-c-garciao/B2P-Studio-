/*
Copyright (c) 2021 - 2022, Juan Carlos Garcia Ojeda, Universidad de Cartagena
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.
   
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package co.edu.unicartagena.b2pstudio.storage;

import co.edu.unicartagena.b2pstudio.objects.Arc;
import co.edu.unicartagena.b2pstudio.objects.Node;
import co.edu.unicartagena.b2pstudio.objects.Canva;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StoreFile {
    
    private Canva project;
    
    public StoreFile(Canva project) {
        this.project = project;
    }
    
    public boolean saveModel(File f) {

        String line;

        try {
            FileWriter file = new FileWriter(f);
            file.write("DEFAULTS\n");
            file.write(project.getMax_routes()+"\n");
            file.write(project.getMax_plans()+"\n");
            file.write(project.getUpper_bound()+"\n");
            file.write("NODES\n");
            
            for(Node node : project.getNodes()){
                line = node.getName()+","+node.getInitial_content()+
                        ","+node.getCapacity()+","+node.getX()+","+node.getY()+"\n";
                file.write(line);
            }

            file.write("ARCS\n");
            
            for(Arc arc : project.getArcs()){
                line = arc.getTravel_time()+","+arc.getCapacity()+
                        ","+arc.getId()+","+arc.getSource()+","+arc.getDestination()+"\n";
                file.write(line);
            }

            file.close();

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public Canva openModel(File f) {
        String line, partes[];
        
        project = new Canva(f.getName());
        boolean estado = true;

        try{
            FileReader fr = new FileReader(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                partes = line.split(",");
                if (line.equalsIgnoreCase("DEFAULTS")) {
                    line = br.readLine();
                    project.setMax_routes(Integer.parseInt(line.trim()));
                    line = br.readLine();
                    project.setMax_plans(Integer.parseInt(line.trim()));
                    line = br.readLine();
                    project.setUpper_bound(Integer.parseInt(line.trim()));
                } else if (line.equalsIgnoreCase("NODES")) {
                    estado = true;
                } else if (line.equalsIgnoreCase("ARCS")) {
                    estado = false;
                } else if (estado == true) {
                    
                    String nombre = partes[0];
                    int contenido = Integer.parseInt(partes[1]);
                    int capacidad = Integer.parseInt(partes[2]);
                    int x = Integer.parseInt(partes[3]);
                    int y = Integer.parseInt(partes[4]);

                    Node node = new Node(contenido, capacidad, nombre, x, y);
                    project.addNode(node);
                } else {
                    int tiempoDesp = Integer.parseInt(partes[0]);
                    int capacidad = Integer.parseInt(partes[1]);
                    int ident = Integer.parseInt(partes[2]);
                    String node1 = partes[3];
                    String node2 = partes[4];

                    Arc arc = new Arc(node1, node2, tiempoDesp, capacidad, ident);
                    project.addArc(arc);
                }
            }

        } catch (IOException e) {
            return null;
        }
        return project;
    }       
}