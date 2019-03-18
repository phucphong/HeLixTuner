package com.example.hp.helixtuner;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class ConVertNote {
    int NotePerOctave = 0;
    int OctaveCount = 7;

    int notes[] = new int[NotePerOctave * OctaveCount];
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
        if (noteName.equals("C2")) {
            return 0;
        } else if (noteName.equals("C#/D♭2")) {
            return 1;
        } else if (noteName.equals("D2")) {
            return 2;
        } else if (noteName.equals("D#/E♭2")) {
            return 3;
        } else if (noteName.equals("E2")) {
            return 4;
        } else if (noteName.equals("F2")) {
            return 5;
        } else if (noteName.equals("F#/G♭2")) {
            return 6;
        } else if (noteName.equals("G2")) {
            return 7;
        } else if (noteName.equals("G#/A♭2")) {
            return 8;
        } else if (noteName.equals("A2")) {
            return 9;
        } else if (noteName.equals("A#/B♭2")) {
            return 10;
        } else if (noteName.equals("B2")) {
            return 11;
        } else if (noteName.equals("C3")) {
            return 12;
        } else if (noteName.equals("C#/D♭3")) {
            return 13;

        } else if (noteName.equals("D3")) {
            return 14;

        } else if (noteName.equals("D#/E♭3")) {
            return 15;

        } else if (noteName.equals("E3")) {
            return 16;

        } else if (noteName.equals("F3")) {
            return 17;

        } else if (noteName.equals("F#/G♭3")) {
            return 18;

        } else if (noteName.equals("G3")) {
            return 19;

        } else if (noteName.equals("G#/A♭3")) {
            return 20;

        } else if (noteName.equals("A3")) {
            return 21;

        } else if (noteName.equals("A#/B♭3")) {
            return 22;

        } else if (noteName.equals("B3")) {
            return 23;

        } else if (noteName.equals("C4")) {
            return 24;

        } else if (noteName.equals("C#/D♭4")) {
            return 25;

        } else if (noteName.equals("D4")) {
            return 26;

        } else if (noteName.equals("D#/E♭4")) {
            return 27;

        } else if (noteName.equals("E4")) {
            return 28;

        } else if (noteName.equals("F4")) {
            return 29;

        } else if (noteName.equals("F#/G♭4")) {
            return 30;

        } else if (noteName.equals("G4")) {
            return 31;

        } else if (noteName.equals("G#/A♭4")) {
            return 32;

        } else if (noteName.equals("A4")) {
            return 33;

        } else if (noteName.equals("A#/B♭4")) {
            return 34;

        } else if (noteName.equals("B4")) {
            return 35;

        } else if (noteName.equals("C5")) {
            return 36;

        } else if (noteName.equals("C#/D♭5")) {
            return 37;

        } else if (noteName.equals("D5")) {
            return 38;

        } else if (noteName.equals("D#/E♭5")) {
            return 39;

        } else if (noteName.equals("E5")) {
            return 40;

        } else if (noteName.equals("F5")) {
            return 41;

        } else if (noteName.equals("F#/G♭5")) {
            return 42;

        } else if (noteName.equals("G5")) {
            return 43;

        } else if (noteName.equals("G#/A♭5")) {
            return 44;

        } else if (noteName.equals("A5")) {
            return 45;

        } else if (noteName.equals("A#/B♭5")) {
            return 46;

        } else if (noteName.equals("B5")) {
            return 47;

        } else if (noteName.equals("C6")) {
            return 48;

        } else if (noteName.equals("C#/D♭6")) {
            return 49;

        } else if (noteName.equals("D6")) {
            return 50;

        } else if (noteName.equals("D#/E♭6")) {
            return 51;

        } else if (noteName.equals("E6")) {
            return 52;

        } else if (noteName.equals("F6")) {
            return 53;

        } else if (noteName.equals("F#/G♭6")) {
            return 54;

        } else if (noteName.equals("G6")) {
            return 55;

        } else if (noteName.equals("G#/A♭6")) {
            return 56;

        } else if (noteName.equals("A6")) {
            return 57;

        } else if (noteName.equals("A#/B♭6")) {
            return 58;

        } else if (noteName.equals("B6")) {
            return 59;

        } else if (noteName.equals("C7")) {
            return 60;

        } else if (noteName.equals("C#/D♭7")) {
            return 61;

        } else if (noteName.equals("D7")) {
            return 62;

        } else if (noteName.equals("D#/E♭7")) {
            return 63;

        } else if (noteName.equals("E7")) {
            return 64;

        } else if (noteName.equals("F7")) {
            return 65;

        } else if (noteName.equals("F#/G♭7")) {
            return 66;

        } else if (noteName.equals("G7")) {
            return 67;

        } else if (noteName.equals("G#/A♭7")) {
            return 68;

        } else if (noteName.equals("A7")) {
            return 69;

        } else if (noteName.equals("A#/B♭7")) {
            return 70;

        } else if (noteName.equals("B7")) {
            return 71;

        } else if (noteName.equals("C8")) {
            return 72;

        } else if (noteName.equals("C#/D♭8")) {
            return 73;

        } else if (noteName.equals("D8")) {
            return 74;

        } else if (noteName.equals("D#/E♭8")) {
            return 75;

        } else if (noteName.equals("E8")) {
            return 76;

        } else if (noteName.equals("F8")) {
            return 77;

        } else if (noteName.equals("F#/G♭8")) {
            return 78;

        } else if (noteName.equals("G8")) {
            return 79;

        }

        else if (noteName.equals("none")) {
            return -1;

        }else
            return 0;
    }
    int convertNameToNoteColorTop(String fullName) {
        String octaveS = fullName.substring(fullName.length() - 1);
        //This app use C2 = 24 midi note, other apps use C0 = 24
        int newOctave = Integer.parseInt(octaveS) + 2;
        String newNoteS = fullName.replace(octaveS, "");
        int newNote = getNoteByNameColorTop(newNoteS);
        return getFullNoteFromNote(newNote, newOctave);
    }
    int getNoteByNameColorTop(String noteName) {
        if (noteName.equals("C2")) {
            return 0;
        } else if (noteName.equals("C#/D♭2")) {
            return 1;
        } else if (noteName.equals("D2")) {
            return 2;
        } else if (noteName.equals("D#/E♭2")) {
            return 3;
        } else if (noteName.equals("E2")) {
            return 4;
        } else if (noteName.equals("F2")) {
            return 5;
        } else if (noteName.equals("F#/G♭2")) {
            return 6;
        } else if (noteName.equals("G2")) {
            return 7;
        } else if (noteName.equals("G#/A♭2")) {
            return 8;
        } else if (noteName.equals("A2")) {
            return 9;
        } else if (noteName.equals("A#/B♭2")) {
            return 10;
        } else if (noteName.equals("B2")) {
            return 11;
        } else if (noteName.equals("C3")) {
            return 0;
        } else if (noteName.equals("C#/D♭3")) {
            return 1;

        } else if (noteName.equals("D3")) {
            return 2;

        } else if (noteName.equals("D#/E♭3")) {
            return 3;

        } else if (noteName.equals("E3")) {
            return 4;

        } else if (noteName.equals("F3")) {
            return 5;

        } else if (noteName.equals("F#/G♭3")) {
            return 6;

        } else if (noteName.equals("G3")) {
            return 7;

        } else if (noteName.equals("G#/A♭3")) {
            return 8;

        } else if (noteName.equals("A3")) {
            return 9;

        } else if (noteName.equals("A#/B♭3")) {
            return 10;

        } else if (noteName.equals("B3")) {
            return 11;

        } else if (noteName.equals("C4")) {
            return 0;

        } else if (noteName.equals("C#/D♭4")) {
            return 1;

        } else if (noteName.equals("D4")) {
            return 2;

        } else if (noteName.equals("D#/E♭4")) {
            return 3;

        } else if (noteName.equals("E4")) {
            return 4;

        } else if (noteName.equals("F4")) {
            return 5;

        } else if (noteName.equals("F#/G♭4")) {
            return 6;

        } else if (noteName.equals("G4")) {
            return 7;

        } else if (noteName.equals("G#/A♭4")) {
            return 8;

        } else if (noteName.equals("A4")) {
            return 9;

        } else if (noteName.equals("A#/B♭4")) {
            return 10;

        } else if (noteName.equals("B4")) {
            return 11;

        } else if (noteName.equals("C5")) {
            return 0;

        } else if (noteName.equals("C#/D♭5")) {
            return 1;

        } else if (noteName.equals("D5")) {
            return 2;

        } else if (noteName.equals("D#/E♭5")) {
            return 3;

        } else if (noteName.equals("E5")) {
            return 4;

        } else if (noteName.equals("F5")) {
            return 5;

        } else if (noteName.equals("F#/G♭5")) {
            return 6;

        } else if (noteName.equals("G5")) {
            return 7;

        } else if (noteName.equals("G#/A♭5")) {
            return 8;

        } else if (noteName.equals("A5")) {
            return 9;

        } else if (noteName.equals("A#/B♭5")) {
            return 10;

        } else if (noteName.equals("B5")) {
            return 11;

        } else if (noteName.equals("C6")) {
            return 0;

        } else if (noteName.equals("C#/D♭6")) {
            return 1;

        } else if (noteName.equals("D6")) {
            return 2;

        } else if (noteName.equals("D#/E♭6")) {
            return 3;

        } else if (noteName.equals("E6")) {
            return 4;

        } else if (noteName.equals("F6")) {
            return 5;

        } else if (noteName.equals("F#/G♭6")) {
            return 6;

        } else if (noteName.equals("G6")) {
            return 7;

        } else if (noteName.equals("G#/A♭6")) {
            return 8;

        } else if (noteName.equals("A6")) {
            return 9;

        } else if (noteName.equals("A#/B♭6")) {
            return 10;

        } else if (noteName.equals("B6")) {
            return 11;

        } else if (noteName.equals("C7")) {
            return 0;

        } else if (noteName.equals("C#/D♭7")) {
            return 1;

        } else if (noteName.equals("D7")) {
            return 2;

        } else if (noteName.equals("D#/E♭7")) {
            return 3;

        } else if (noteName.equals("E7")) {
            return 4;

        } else if (noteName.equals("F7")) {
            return 5;

        } else if (noteName.equals("F#/G♭7")) {
            return 6;

        } else if (noteName.equals("G7")) {
            return 7;

        } else if (noteName.equals("G#/A♭7")) {
            return 8;

        } else if (noteName.equals("A7")) {
            return 9;

        } else if (noteName.equals("A#/B♭7")) {
            return 10;

        } else if (noteName.equals("B7")) {
            return 11;

        } else if (noteName.equals("C8")) {
            return 0;

        } else if (noteName.equals("C#/D♭8")) {
            return 1;

        } else if (noteName.equals("D8")) {
            return 2;

        } else if (noteName.equals("D#/E♭8")) {
            return 3;

        } else if (noteName.equals("E8")) {
            return 4;

        } else if (noteName.equals("F8")) {
            return 5;

        } else if (noteName.equals("F#/G♭8")) {
            return 6;

        } else if (noteName.equals("G8")) {
            return 7;

        }

        else if (noteName.equals("none")) {
            return -1;

        }else
            return 0;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    String getNoteNameByNote(int fullNote) {
        int noteWithoutOctave = Math.floorMod(fullNote, NotePerOctave);
        int sOctave = getOctaveFromNote(fullNote);


        return getNoteNameByNote(noteWithoutOctave, sOctave);
    }

    String getImgNameByNote(int n) {
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

    String getNoteNameByNote(int noteNo, int octaveNo) {
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
        if (octaveNo >= 0)
            return (String.format("%@%d", name, octaveNo));
        else {
            return name;
        }
    }

}
