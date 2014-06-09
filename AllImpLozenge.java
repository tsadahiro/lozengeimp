import java.util.*;

class AllImpLozenge{

    ImpLozenge tiling;
    HashSet<Integer> visited;

    AllImpLozenge(int size){
	tiling = new ImpLozenge(size);
	visited = new HashSet<Integer>();
    }

    void search(){

	if (!visited.add(tiling.hashCode())){
	    return;
	}

	System.out.println(tiling.toString());
	//System.out.println(tiling.impurities);
	///System.out.println(tiling.isValid());
	if (!tiling.isValid()) ;
	HashMap<Coord,HashSet<Move>> possiblemoves = tiling.getMovable();
	//System.out.println(possiblemoves);
	for (Map.Entry<Coord,HashSet<Move>> moves: possiblemoves.entrySet()){
	    Coord c = moves.getKey();
	    
	    for (Iterator iter = moves.getValue().iterator(); iter.hasNext();){
		Move m = (Move)(iter.next());
		Move reverse = new Move(m.type, tiling.dir.get(c));
		//System.out.print(tiling.dir);
		//System.out.println(" --> move: " + m + " reverse:" + reverse + " at " + c);
		tiling.move(c,m);
		search();
		tiling.move(c,reverse);
	    }
	}
    }

    public static void main(String[] args){

	AllImpLozenge all = new AllImpLozenge(3);
	all.search();

    }
}
