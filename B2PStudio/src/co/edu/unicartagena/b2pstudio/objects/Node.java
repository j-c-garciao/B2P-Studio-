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

package co.edu.unicartagena.b2pstudio.objects;

import co.edu.unicartagena.b2pstudio.objects.NodeType.Type;

public class Node {
    private int initial_content;
    private int capacity;
    private String name;
    private int x;
    private int y;
    private Type type;
    boolean ghost;
    boolean child;

    public Node() {
    }

    public Node(int initial_content, int capacity, String name, int x, int y) {
        this.initial_content = initial_content;
        this.capacity = capacity;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getInitial_content() {
        return initial_content;
    }

    public void setInitial_content(int initial_content) {
        this.initial_content = initial_content;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isGhost() {
        return ghost;
    }

    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }
    
    public void setChild(boolean child) {
        this.child = child;
    }
    
    public boolean isChild() {
        return child;
    }
    
    @Override
    public String toString(){
        return String.format("Node [name = %s,\n"+
                             "     initial content= %s,\n"+
                             "     capacity = %s,\n"+
                             "     x = %s,\n"+
                             "     y = %s]",
                             name,initial_content,capacity,x,y);
    }
}