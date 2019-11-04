package server.model;

public enum Srv_Rank { //@author Sandro
    Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace, Phoenix, Dragon, Dog, Mahjong;

    public String toString() {
        String rank = "";
        switch (this) {
            case Two: rank = "2"; break;
            case Three: rank = "3"; break;
            case Four: rank = "4"; break;
            case Five: rank = "5"; break;
            case Six: rank = "6"; break;
            case Seven: rank = "7"; break;
            case Eight: rank = "8"; break;
            case Nine: rank = "9"; break;
            case Ten: rank = "10"; break;
            case Jack: rank = "jack"; break;
            case Queen: rank = "queen"; break;
            case King: rank = "king"; break;
            case Ace: rank = "ace"; break;
            case Phoenix: rank = "phoenix"; break;
            case Dragon: rank = "dragon"; break;
            case Dog: rank = "dog"; break;
            case Mahjong: rank = "mahjong"; break;
        }
        return rank;
    }
}
