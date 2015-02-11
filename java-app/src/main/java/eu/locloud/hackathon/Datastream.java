package eu.locloud.hackathon;

public class Datastream {
    private int itemId;
    private String uri;

    public Datastream() {
        
    }
     
    public Datastream(int itemId, String uri) {
        this.itemId = itemId;
        this.uri = uri;
    }
    
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Datastream [itemId=" + itemId + ", uri=" + uri + "]";
    }

}
