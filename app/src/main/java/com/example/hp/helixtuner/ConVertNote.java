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
        else if (noteName.equals("A1")) {
            return 9;

        } else if (noteName.equals("A#/B♭1")) {
            return 10;

        } else if (noteName.equals("B1")) {
            return 11;

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
                return "A1";

            case 1:
                return "A#/B♭1";

            case 2:
                return "B1";

            case 3:
                return "C2";

            case 4:
                return "C#/D♭2";

            case 5:
                return "D2";

            case 6:
                return "D#/E♭2";

            case 7:
                return "E2";

            case 8:
                return "F2";

            case 9:
                return "F#/G♭2";

            case 10:
                return "G2";

            case 11:
                return "G#/A♭2";
            case 12:
                return "A2";

            case 13:
                return "A#/B♭2";

            case 14:
                return "B2";

            case 15:
                return "C3";

            case 16:
                return "C#/D♭3";

            case 17:
                return "D3";

            case 18:
                return "D#/E♭3";

            case 19:
                return "E3";

            case 20:
                return "F3";

            case 21:
                return "F#/G♭3";

            case 22:
                return "G3";

            case 23:
                return "G#/A♭3";

            case 24:
                return "A3";

            case 25:
                return "A#/B♭3";

            case 26:
                return "B3";

            case 27:
                return "C4";

            case 28:
                return "C#/D♭4";

            case 29:
                return "D4";

            case 30:
                return "D#/E♭4";

            case 31:
                return "E4";

            case 32:
                return "F4";

            case 33:
                return "F#/G♭4";

            case 34:
                return "G4";

            case 35:
                return "G#/A♭4";

            case 36:
                return "A4";

            case 37:
                return "A#/B♭4";

            case 38:
                return "B4";

            case 39:
                return "C5";

            case 40:
                return "C#/D♭5";

            case 41:
                return "D5";

            case 42:
                return "D#/E♭5";

            case 43:
                return "E5";

            case 44:
                return "F5";

            case 45:
                return "F#/G♭5";
                //
            case 46:
                return "G5";

            case 47:
                return "G#/A♭5";

            case 48:
                return "A5";

            case 49:
                return "A#/B♭5";

            case 50:
                return "B5";

            case 51:
                return "C6";

            case 52:
                return "C#/D♭6";

            case 53:
                return "D6";

            case 54:
                return "D#/E♭6";

            case 55:
                return "E6";

            case 56:
                return "F6";

            case 57:
                return "F#/G♭6";


            case 58:
                return "G6";

            case 59:
                return "G#/A♭6";

            case 60:
                return "A6";

            case 61:
                return "A#/B♭6";

            case 62:
                return "B6";

            case 63:
                return "C7";

            case 64:
                return "C#/D♭7";

            case 65:
                return "D7";

            case 66:
                return "D#/E♭7";

            case 67:
                return "E7";

            case 68:
                return "F7";

            case 69:
                return "F#/G♭7";

                //
            case 70:
                return "G7";

            case 71:
                return "G#/A♭7";

            case 72:
                return "A7";

            case 73:
                return "A#/B♭7";

            case 74:
                return "B7";

            case 75:
                return "C8";

            case 76:
                return "C#/D♭8";

            case 77:
                return "D8";

            case 78:
                return "D#/E♭8";

            case 79:
                return "E8";

            case 80:
                return "F8";

            case 81:
                return "F#/G♭8";
                //
            case 82:
                return "G8";

            case 83:
                return "G#/A♭8";


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
