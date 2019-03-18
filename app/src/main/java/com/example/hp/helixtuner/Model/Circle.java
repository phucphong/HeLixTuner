package com.example.hp.helixtuner.Model;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.example.hp.helixtuner.ValidatePublic.*;


public class Circle {
    private int mProgram, mPositionHandle, mColorHandle, mMVPMatrixHandle;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer colorBuffer;
    int vertexShader;
    int fragmentShader;
    //    float radius = 0.6f;
//    int Stride_2D =2;
//    int spp =500;
//    float circleBuffers[] = new float[spp*2];
    private int circleSize = 361;
    ;//   poitn
    private int verticeSize = circleSize * 2;// gllineS noi 2 diem gan nhau
    private float vertices[] = new float[verticeSize * 6];// 1 poitn co x,y,z
    float color[] = {
            0.0f, 0.0f, 1.0f, 1.0f}; // Blue (NEW)};
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public Circle() {
        int jdem = 1;
        for (int i = 0; i < verticeSize; i++) {
            vertices[(i * 6) + 0] = (float) (0.5 * Math.cos(jdem * ((Math.PI / 180))) * (0.7 + circleBuffers[i]));
            vertices[(i * 6) + 1] = (float) (0.5 * Math.sin(jdem * ((Math.PI / 180))) * (0.7 + circleBuffers[i]));
            vertices[(i * 6) + 2] = 0.0f;

            vertices[(i * 6) + 3] = (float) (0.5 * Math.cos(jdem * ((Math.PI / 180))) * (0.7 + circleBuffers[i + 1]));
            vertices[(i * 6) + 4] = (float) (0.5 * Math.sin(jdem * ((Math.PI / 180))) * (0.7 + circleBuffers[i + 1]));
            vertices[(i * 6) + 5] = 0.0f;
            jdem += 1;


        }
        int mbyteonfloat = 4;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect((vertices.length) * mbyteonfloat);
        // use the device hardware's native byte order
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        // create a floating point buffer from the ByteBuffer
        mVertexBuffer = vertexByteBuffer.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        mVertexBuffer.put(vertices);
        // set the buffer to read the first coordinate
        mVertexBuffer.position(0);

        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();



        // create empty OpenGL ES Program
        //  mProgram=GLES20.g
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);








    }

    public static int loadShader(int type, String shaderCode) {

        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
//    public void prepareCircleArray(int spp){
//
//
//        for(int i=0;i<spp*Stride_2D;i+=Stride_2D){
//            //x
//            circleBuffers[i]  = (float) Math.cos(2.0*Math.PI*(((float)i/Stride_2D)/(spp)));
//            //y
//            circleBuffers[i+1]  = (float) Math.cos(2.0*Math.PI*(((float)i/Stride_2D)/(spp)));
//        }
//
//    }


    public void draw(float[] mvpMatrix) {

        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the circle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false, 0
                , mVertexBuffer);


        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // Set color for drawing the square
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the circle

        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, verticeSize);
        GLES20.glLineWidth(10);


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

}