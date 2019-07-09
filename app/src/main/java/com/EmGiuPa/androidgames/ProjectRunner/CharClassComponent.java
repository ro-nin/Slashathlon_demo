package com.EmGiuPa.androidgames.ProjectRunner;

public class CharClassComponent extends Component {
    private ClassTypeEnum charClass;


    public ClassTypeEnum getCharClass() {
        return charClass;
    }

    public void setCharClass(ClassTypeEnum charClass) {
        this.charClass = charClass;
    }

    CharClassComponent(ClassTypeEnum charClass) {
        this.charClass = charClass;
    }

    @Override
    public ComponentTypeEnum type() {
        return ComponentTypeEnum.CLASS;
    }

    @Override
    public String toString() {
        if (charClass == ClassTypeEnum.A)
            return "A";
        if (charClass == ClassTypeEnum.B)
            return "B";
        if (charClass == ClassTypeEnum.C)
            return "C";
        return null;
    }
}
