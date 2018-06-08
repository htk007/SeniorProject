package com.example.dell.augmentedreality;

/**
 * Created by Dell on 23.5.2016.
 */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;

class Cube{
    public Cube()
    {        int one = 0x10000;
        int vertices[] = {
                -one, -one, -one,
                one, -one, -one,
                one,  one, -one,
                -one,  one, -one,
                -one, -one,  one,
                one, -one,  one,
                one,  one,  one,
                -one,  one,  one,        };

        float[] colors = {
                0f,  0f,     0f, 0.5f,
                1f,  0f,     0f, 0.1f,
                1f,  1f,     0f, 0.5f,
                0f,  1f,     0f, 0.1f,
                0f,  0f,     1f, 0.1f,
                1f,  0f,     1f, 0.2f,
                1f,  1f,     1f, 0.1f,
                0f,  1f,     1f,  0.1f,        };

        byte indices[] = {
                0, 4, 5,    0, 5, 1,
                1, 5, 6,    1, 6, 2,
                2, 6, 7,    2, 7, 3,
                3, 7, 4,    3, 4, 0,
                4, 7, 6,    4, 6, 5,
                3, 0, 1,    3, 1, 2        };


        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);    }
    public void draw(GL10 gl)    {
        gl.glFrontFace(gl.GL_CW);
        gl.glVertexPointer(3, gl.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, gl.GL_FIXED, 0, mColorBuffer);
        gl.glDrawElements(gl.GL_TRIANGLES, 36, gl.GL_UNSIGNED_BYTE, mIndexBuffer);    }

    private IntBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer  mIndexBuffer;

}
