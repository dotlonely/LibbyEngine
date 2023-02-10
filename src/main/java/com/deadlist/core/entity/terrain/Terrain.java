package com.deadlist.core.entity.terrain;

import com.deadlist.core.ObjectLoader;
import com.deadlist.core.entity.Material;
import com.deadlist.core.entity.Model;
import com.deadlist.core.entity.Texture;
import com.deadlist.core.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.temporal.ValueRange;

public class Terrain {

    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 40;
    private static final float MIN_HEIGHT = -40;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;



    private Vector3f position;
    private Model model;

    private TerrainTexturePack terrainTexturePack;
    private TerrainTexture blendMap;

    private float[][] heights;

    public Terrain(Vector3f position, ObjectLoader loader, Material material, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap, String heightMap) {
        this.position = position;
        this.model = generateTerrain(loader, heightMap);
        this.terrainTexturePack = terrainTexturePack;
        this.blendMap = blendMap;
    }

    private Model generateTerrain(ObjectLoader loader, String heightMap){

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("textures/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int VERTEX_COUNT = image.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = j / (VERTEX_COUNT - 1.0f) * SIZE;
                float height = getHeight(j, i, image);
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height; //height map
                vertices[vertexPointer * 3 + 2] = i / (VERTEX_COUNT - 1.0f) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] =normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = j / (VERTEX_COUNT - 1.0f);
                textureCoords[vertexPointer * 2 + 1] = i / (VERTEX_COUNT - 1.0f);
                vertexPointer++;
            }
        }

        int pointer = 0;
        for (int z = 0; z < VERTEX_COUNT - 1.0f; z++) {
            for (int x = 0; x < VERTEX_COUNT - 1.0f; x++) {
                int topLeft = (z * VERTEX_COUNT) + x;
                int topRight = topLeft + 1;
                int bottomLeft = ((z + 1) * VERTEX_COUNT) + x;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadModel(vertices, textureCoords, normals, indices);

    }

    private float getHeight(int x, int z, BufferedImage image){
        if(x < 0  || x >= image.getHeight() || z < 0 || z >= image.getHeight()){
            return 0;
        }
        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR / 2;
        height /= MAX_PIXEL_COLOR / 2;
        height *= MAX_HEIGHT;
        return height;
    }

    public float getHeightOfTerrain(float worldX, float worldZ){
        float terrainX = worldX - this.position.x;
        float terrainZ = worldZ - this.position.z;
        float gridSquareSize = SIZE / ((float)heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0){
            return 0;
        }

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;
        if(xCoord <= (1 - zCoord)){
            answer = Utils.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                    heights[gridX + 1][gridZ], 0), new Vector3f(0,
                    heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }else{
            answer = Utils.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                    heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                    heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }

        return answer;
    }


    private Vector3f calculateNormal(int x, int z, BufferedImage heightMap){
        float heightL = getHeight(x+1, z, heightMap);
        float heightR = getHeight(x-1, z, heightMap);
        float heightU = getHeight(x, z+1, heightMap);
        float heightD = getHeight(x, z-1, heightMap);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Model getModel() {
        return model;
    }

    public Material getMaterial(){
        return model.getMaterial();
    }

    public TerrainTexturePack getTerrainTexturePack(){
        return terrainTexturePack;
    }
    public TerrainTexture getBlendMap(){
        return blendMap;
    }

}
