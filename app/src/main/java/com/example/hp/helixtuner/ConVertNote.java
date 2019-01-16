package com.example.hp.helixtuner;

import android.os.Build;
import android.support.annotation.RequiresApi;

public class ConVertNote {
    int NotePerOctave;
    int OctaveCount = 7;
    int notes = OctaveCount * NotePerOctave;
    int selectedNotes;
    int getOctaveFromNote(int newNote) {
        return (int) Math.floor(newNote / NotePerOctave);
    }

    int getFullNoteFromNote(int newNote, int newOctave) {
        return newOctave * NotePerOctave + newNote;
    }

    int convertNameToNote(String fullName) {
        String octaveS = fullName.substring(fullName.length() - 1);
        //This app use C2 = 24 midi note, other apps use C0 = 24
        int newOctave = Integer.parseInt(octaveS) + 2;
        String newNoteS = fullName.replace(octaveS, "");
        int newNote = getNoteByName(newNoteS);
        return getFullNoteFromNote(newNote, newOctave);
    }
    double convertNoteToFrequency(float note, float octave) {
        return 440 * Math.pow(2, (note - 69 - octave * 12) / 12.);
    }


    int getNoteByName(String noteName) {
        if (noteName.equals("C")) {
            return 0;
        } else if (noteName.equals("C#") || (noteName.equals("Db"))) {
            return 1;
        } else if (noteName.equals("D")) {
            return 2;
        } else if (noteName.equals("D#") || (noteName.equals("Eb"))) {
            return 3;
        } else if (noteName.equals("E")) {
            return 4;
        } else if (noteName.equals("F")) {
            return 5;
        } else if (noteName.equals("F#") || (noteName.equals("Gb"))) {
            return 6;
        } else if (noteName.equals("G")) {
            return 7;
        } else if (noteName.equals("G#") || (noteName.equals("Ab"))) {
            return 8;
        } else if (noteName.equals("A")) {
            return 9;
        } else if (noteName.equals("A#") || (noteName.equals("Bb"))) {
            return 10;
        } else if (noteName.equals("B")) {
            return 11;
        } else
            return 0;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    String getNoteNameByNote(int fullNote){
        int noteWithoutOctave = Math.floorMod(fullNote, NotePerOctave);
        int sOctave =getOctaveFromNote(fullNote);
        return getNoteNameByNote(noteWithoutOctave,sOctave);
    }
    String getImgNameByNote(int n){
        switch (n) {
            case 0:
                return "C";

            case 1:
                return "C#";

            case 2:
                return "D";

            case 3:
                return "D#";

            case 4:
                return "E";

            case 5:
                return "F";

            case 6:
                return "F#";

            case 7:
                return "G";

            case 8:
                return "G#";

            case 9:
                return "A";

            case 10:
                return "A#";

            case 11:
                return "B";

            default:
                break;
        }
        return "";
    }
    String getNoteNameByNote(int noteNo ,int octaveNo){
        String name = "";
        switch (noteNo) {
            case 0:
                name = "C";
                break;
            case 1:
                name = "C#/D♭";
                break;
            case 2:
                name = "D";
                break;
            case 3:
                name = "D#/E♭";
                break;
            case 4:
                name = "E";
                break;
            case 5:
                name = "F";
                break;
            case 6:
                name = "F#/G♭";
                break;
            case 7:
                name = "G";
                break;
            case 8:
                name = "G#/A♭";
                break;
            case 9:
                name = "A";
                break;
            case 10:
                name = "A#/B♭";
                break;
            case 11:
                name = "B";
                break;
            default:
                break;
        }
        if(octaveNo>=0)
            return (String.format("%@%d",name,octaveNo));
        else{
            return name;
        }
    }

}
