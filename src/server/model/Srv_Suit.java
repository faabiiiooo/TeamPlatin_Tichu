package server.model;

public enum Srv_Suit {
    Jade, Pagodas, Stars, Swords, SpecialCards;

    public String toString() {
        String suit = "";
        switch (this) {
            case Jade: suit = "jade"; break;
            case Pagodas: suit = "pagodas"; break;
            case Stars: suit = "stars"; break;
            case Swords: suit = "swords"; break;
            case SpecialCards: suit = "specialCards"; break;
        }
        return suit;
    }
}

