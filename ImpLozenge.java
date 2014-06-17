
import java.util.*;

public class ImpLozenge{

    public int size;
    public HashMap<Coord,Integer> dir;
    public HashMap<Coord,Integer> impurities;
    static int typeA = 1, typeB = 2, typeC = 3;

    ImpLozenge(int s){
	size = s; // length of an edge of the triangle
	dir = new HashMap<Coord,Integer>();
	impurities = new HashMap<Coord,Integer>();

	for (int i = 0; i < size; i++){
	    for (int j = 0; j < size -i; j++){
		Coord c = new Coord(i,j);
		HashSet<Move> moveset = new HashSet<Move>();
		dir.put(new Coord(i,j),0); // initial configuration
	    }
	}
	for (int i = 0; i < size; i+=2){
	    Coord c = new Coord(i, size - i -1);
	    impurities.put(c,0);
	}
    }


    ImpLozenge(String s){
	size = Integer.parseInt(s); // length of an edge of the triangle
	dir = new HashMap<Coord,Integer>();
	impurities = new HashMap<Coord,Integer>();

	for (int i = 0; i < size; i++){
	    for (int j = 0; j < size -i; j++){
		Coord c = new Coord(i,j);
		HashSet<Move> moveset = new HashSet<Move>();
		dir.put(new Coord(i,j),0); // initial configuration
	    }
	}
	for (int i = 0; i < size; i+=2){
	    Coord c = new Coord(i, size - i -1);
	    impurities.put(c,0);
	}
    }

    String dir(){
	return dir.toString();
    }

    void move(Coord c, Move m){

	if (m.type == typeA){ // rotate a unit triangle
	    dir.put(c, m.dir);
	    impurities.put(c, m.dir);
	}
	else if (m.type == typeB){
	    int[] ds =     {0,1,0,2,1,2}; // current direction from white
	    int[] newdir = {1,0,2,0,2,1}; // new     direction from white
	    int[] dx =     {0,1,-1,-1,1,0};
	    int[] dy =     {-1,-1,0,1,0,1};
	    int[] ox =     {1,0,-1,-1,0,1};
	    int[] oy =     {-1,-1,1,0,1,0};

	    for (int i = 0; i < ds.length; i++){
		Coord newsite = new Coord(c.x + dx[i], c.y + dy[i]);
		Coord oldsite = new Coord(c.x + ox[i], c.y + oy[i]);
		if (dir.get(c) == ds[i] & m.dir == newdir[i]){
		    dir.put(c,m.dir);
		    impurities.remove(oldsite);
		    impurities.put(newsite,ds[i]);
		    break;
		}
	    }
	}
	else if (m.type == typeC){
	    Coord d = new Coord(c.x+1,c.y);
	    Coord e = new Coord(c.x,c.y+1);
	    if (m.dir == 1){
		dir.put(c,1);
		dir.put(d,2);
		dir.put(e,0);
	    }
	    else if (m.dir == 2){
		dir.put(c,2);
		dir.put(d,0);
		dir.put(e,1);
	    }
	}




	//else if (m.type == typeC){
	//    int[] ds =     {1,2}; // current direction from white
	//    int[] newdir = {2,1}; // new     direction from white
	//    int[] vdir =   {0,2};
	//    int[] wdir =   {1,0};
	//
	//    for (int i = 0; i < ds.length; i++){
	//	Coord v = new Coord(c.x + 1, c.y);
	//	Coord w = new Coord(c.x, c.y + 1);
	//	if (dir.get(c) == ds[i] & m.dir == newdir[i]){
	//	    dir.put(c,m.dir);
	//	    dir.put(v,vdir[i]);
	//	    dir.put(w,wdir[i]);
	//	    break;
	//	}
	//    }
	//
	//}
    }




    HashMap<Coord,HashSet<Move>>  getMovable(){
	HashMap<Coord,HashSet<Move>> possiblemoves = new HashMap<Coord,HashSet<Move>>();
	for (int i = 0; i < size; i++){
	    for (int j = 0; j < size -i; j++){
		Coord c = new Coord(i,j);
		HashSet<Move> moveset = new HashSet<Move>();
		possiblemoves.put(c, moveset); // insert the empty set
	    }
	}
	for (Map.Entry<Coord,Integer> e: dir.entrySet()){
	    getTypeAaround(e.getKey(), possiblemoves);
	    getTypeBaround(e.getKey(), possiblemoves);
	    getTypeCaround(e.getKey(), possiblemoves);
	}
	return possiblemoves;
    }

    void getTypeAaround(Coord c, HashMap<Coord,HashSet<Move>> possiblemoves){ 

	if (impurities.get(c) != null && impurities.get(c) == dir.get(c)){
	    possiblemoves.get(c).add(new Move(typeA, (dir.get(c)+1)%3));
	    possiblemoves.get(c).add(new Move(typeA, (dir.get(c)+2)%3));
	}
    }

    void getTypeBaround(Coord c, HashMap<Coord,HashSet<Move>> possiblemoves){
	int[] ds = {0,0,1,1,2,2};
	int[] dx = {-1,1,0,0,1,-1};
	int[] dy = {1,-1,-1,1,0,0};
	int[] impdir = {2,1,0,2,1,0};
	int[] newdir = {2,1,0,2,1,0};

	for (int i = 0; i < dx.length; i++){
	    Coord f = new Coord(c.x + dx[i], c.y + dy[i]);
	    if (dir.get(c) == ds[i] && impurities.get(f) != null && impurities.get(f) == impdir[i]){
		possiblemoves.get(c).add(new Move(typeB, newdir[i]));
		return;
	    }
	}
    }

    void getTypeCaround(Coord c, HashMap<Coord,HashSet<Move>> possiblemoves){
	Coord d = new Coord(c.x+1, c.y);
	Coord e = new Coord(c.x, c.y+1);

	if ((dir.get(c)!=null && dir.get(c) == 2) && 
	    (dir.get(d)!=null && dir.get(d) == 0) && 
	    (dir.get(e)!=null && dir.get(e) == 1)){
	    possiblemoves.get(c).add(new Move(typeC, 1));
	}
	if ((dir.get(c)!=null && dir.get(c) == 1) && 
	    (dir.get(d)!=null && dir.get(d) == 2) && 
	    (dir.get(e)!=null && dir.get(e) == 0)){
	    possiblemoves.get(c).add(new Move(typeC, 2));
	}
    }

    /*
    void getTypeCaround(Coord c, int direction, HashMap<Coord,HashSet<Move>> possiblemoves){
	int[] ds = {0,0,1,1,2,2};
	int[] dx = {-1,1,0,0,1,-1};
	int[] dy = {1,-1,-1,1,0,0};
	int[] impdir = {2,1,0,2,1,0};
	int[] newdir = {2,1,0,2,1,0};

	Coord v = new Coord(c.x + 1, c.y);
	Coord w = new Coord(c.x, c.y + 1);

	if (dir.get(v) != null && dir.get(w) != null && 
	    dir.get(c) == 1 && dir.get(v) == 2 && dir.get(w) == 0){
	    possiblemoves.get(c).add(new Move(typeC, 2));
	    //System.out.println("typeC");
	}
	if (dir.get(v) != null && dir.get(w) != null && 
	    dir.get(c) == 2 && dir.get(v) == 0 && dir.get(w) == 1){
	    //possiblemoves.get(c).add(new Move(typeC, 1));
	}
    }
    */



    @Override
    public String toString(){
	String str = "";
	Coord c;
	for (int j = 0; j < size; j++){
	    for (int i = 0; i < size; i++){
		c = new Coord(i,j);
		if (dir.get(c)==null){
		    continue;
		}
		str += dir.get(c);
	    }
	}
	return str;
    }

    @Override
    public int hashCode(){
	int hc = 0;
	for (Map.Entry<Coord,Integer> e: dir.entrySet()){
	    hc = hc*3 + e.getValue();
	}
	return hc;
    }

    @Override
    public boolean equals(Object obj){
	return(this.hashCode()==((ImpLozenge)obj).hashCode());
    }

    public static void main(String[] args){
	ImpLozenge tiling = new ImpLozenge(3);
    }

}

class Coord{
    int x;
    int y;

    Coord(int x, int y){
	this.x = x;
	this.y = y;
    }

    @Override
    public String toString(){
	return(x + "," + y);
    }

    @Override
    public boolean equals(Object obj){
	return(this.hashCode()==((Coord)obj).hashCode());
    }
    
    @Override
    public int hashCode(){
	return 10000*x + y;
    }

	    
}

class Move{

    int type;
    int dir; // 0 west, 1 south-east, 2 north-east

    Move(int t, int a){
	this.type = t;
	this.dir = a;
    }

    @Override
    public String toString(){
	if (type == ImpLozenge.typeA){
	    return("A: " + dir);
	}
	else if (type == ImpLozenge.typeB){
	    return("B: " + dir);
	}
	else{
	    return("C: " + dir);
	}
    }

    @Override
    public boolean equals(Object obj){
	return(this.hashCode()==((Move)obj).hashCode());
    }
    
    @Override
    public int hashCode(){
	return 10*type + dir;
    }

}
