/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.falkensmaze.model;

import com.google.gson.Gson;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;


import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import pedro.ieslaencanta.com.falkensmaze.Size;

/**
 *
 * @author Pedro
 */
@XmlRootElement
public class Maze implements Serializable {

    private Size size;
    private Block[][] blocks;
    private double time;
    private String sound;
    private Path path;

    public Maze() {
    }

    public void init() {
        this.setBlocks(new Block[this.getSize().getHeight()][this.getSize().getWidth()]);
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                this.getBlocks()[i][j] = new Block();

            }
        }
    }

    public void reset() {
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                this.getBlocks()[i][j] = null;

            }
        }
        this.setBlocks(null);
    }

    public void reset(Size newsize) {
        this.reset();;
        this.setSize(newsize);
        this.init();
    }

    public void setBlockValue(String value, int row, int col) {
        this.getBlocks()[col][row].setValue(value);
    }

    public String getBlockValue(int row, int col) {
        return this.getBlocks()[row][col].getValue();
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public static Maze load(File file) throws IOException, ClassNotFoundException, JAXBException {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        Maze m = null;
        switch (extension) {
            case "bin":
                m = loadBin(file);
                break;
            case "json":
                m = loadJSON(file);
                break;
            case "xml":
                m = loadXML(file);
                break;
            default:
                break;
        }
        return m;

    }

    public static void save(Maze maze, File file) throws Exception {
        if (maze.sound == null || maze.sound.equals("")) {
            throw new Exception("Es necesario indicar el sonido del laberinto");
        }
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        switch (extension) {
            case "bin":
                Maze.saveBin(maze, file);
                break;
            case "json":
                Maze.saveJSON(maze, file);
                break;
            case "xml":
                Maze.saveXML(maze, file);
                break;
            default:
                break;
        }
      
    }
    //cargar en Json
    private static Maze loadJSON(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        Maze m = gson.fromJson(new FileReader(file), Maze.class);
        return m;
    }
    //cargar en XML
    private static Maze loadXML(File file) throws JAXBException {
        JAXBContext jaxbContext;
        jaxbContext = JAXBContext.newInstance(Maze.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Maze m = (Maze) jaxbUnmarshaller.unmarshal(file);
        return m;
    }
    //cargar en binario
    public static Maze loadBin(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream entrada = new ObjectInputStream(fis);
        Maze maze = (Maze) entrada.readObject();
        return maze;
    }
    //Guardar en JSON
    private static void saveJSON(Maze maze, File file) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(maze);
        PrintWriter p = new PrintWriter(file);
        p.print(json);
        p.close();
    }
    //Guardar en XML
    private static void saveXML(Maze maze, File file) throws FileNotFoundException, JAXBException {
        JAXBContext contexto = JAXBContext.newInstance(maze.getClass());
        Marshaller marshaller = contexto.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
        marshaller.marshal(maze,file);
    }

    //Guardar en binario
    public static void saveBin(Maze maze, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(maze);
        oos.close();
    }
}
