package com.example.hp.helixtuner;

import android.os.Build;
import android.renderscript.Float3;
import android.support.annotation.RequiresApi;

public class BHSVtoRGB {

    public static float NoteToHue(float n) {
        return ((n * 7.0f + 7.5f) / 12.0f);
    }

    public static Float3 bHSVZeroH = new Float3(0.5f, 3.5f / 3, 2.5f / 3);

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Float3 bHSVToRGB(float hue, float sat, float value) {
        Float3 bHSV = new Float3();
        bHSV.x = hue;
        bHSV.y = sat;
        bHSV.z = value;
        //   get the pure hue as an RGB colour
        Float3 hueRGB = new Float3();

        Float3 sumBhsvZezoH = new Float3();
        sumBhsvZezoH.x = (6 * Math.abs(fract(bHSVZeroH.x + bHSV.x) - 0.5f)) - 1.0f;
        sumBhsvZezoH.y = (6 * Math.abs(fract(bHSVZeroH.y + bHSV.x) - 0.5f)) - 1.0f;
        sumBhsvZezoH.z = (6 * Math.abs(fract(bHSVZeroH.z + bHSV.x) - 0.5f)) - 1.0f;
        hueRGB = saturate3(sumBhsvZezoH);
        // emphasize the Yellow, Cyan, Magenta
        Float3 huePow = powf(hueRGB, 1.5f);
        hueRGB.x = 1.0f - huePow.x;
        hueRGB.y = 1.0f - huePow.y;
        hueRGB.z = 1.0f - huePow.z;
        // balance the value so that yellow and magenta fade to black at a similar rate
        float gamma = Math.abs(fract((float) (Math.sqrt(bHSV.x) + 0.5)) * 2.0f - 1.0f) * bHSV.y;
        Float3 hueRgbtruMotnhanBhsvcongMot = new Float3();
        hueRgbtruMotnhanBhsvcongMot.x = (((hueRGB.x - 1.0f) * bHSV.y) + 1.0f);
        hueRgbtruMotnhanBhsvcongMot.y = (((hueRGB.y - 1.0f) * bHSV.y) + 1.0f);
        hueRgbtruMotnhanBhsvcongMot.z = (((hueRGB.z - 1.0f) * bHSV.y) + 1.0f);
        float gm = gamma + 1.0f;

        Float3 powGama = pow(bHSV, gm);

        //  vector_float3 rgb = ((hueRGB - 1.) * bHSV.y + 1.) * pow(bHSV.z, 1.0 + gamma);
        Float3 rgb = MulFloat3(hueRgbtruMotnhanBhsvcongMot, powGama);
        // apply saturation and value
        return rgb;
    }

    public static Float3 powf(Float3 v, float p) {
        Float3 powfFloat3 = new Float3();
        powfFloat3.x = (float) Math.pow(v.x, p);
        powfFloat3.y = (float) Math.pow(v.y, p);
        powfFloat3.z = (float) Math.pow(v.z, p);

        return powfFloat3;

    }

    public static Float3 pow(Float3 a, float b) {
        Float3 powFloat3 = new Float3();
        powFloat3.z = (float) Math.pow(a.z, b);


        return powFloat3;

    }

    public static Float3 MulFloat3(Float3 a, Float3 b) {
        Float3 MulFloat3 = new Float3();
        MulFloat3.x = a.x * b.z;
        MulFloat3.y = a.y * b.z;
        MulFloat3.z = a.z * b.z;


        return MulFloat3;

    }

    public static Float3 saturate3(Float3 v) {
        Float3 satV = new Float3();
        satV.x = saturate(v.x);
        satV.y = saturate(v.y);
        satV.z = saturate(v.z);
        return satV;
    }

    public static float saturate(float x) {

        if (x > 1.) return 1.f;
        if (x < 0.) return 0.f;
        return x;

    }

    public static float fract(float x) {
        return (float) (x - Math.floor(x));
    }

    public static Float3 fract3(Float3 v) {
        Float3 fractV = new Float3();
        fractV.x = fract(v.x);
        fractV.y = fract(v.y);
        fractV.z = fract(v.z);
        return fractV;
    }
}
