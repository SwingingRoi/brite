package Export;

import Graph.*;
import Model.ModelConstants;
import Topology.Topology;
import Util.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
//bacially we dont change the format of file
 /* 
 *Export.BriteExport provides functionality to export a topology into
 * a BRITE format file.  The BRITE format looks like:
 * <br>
 *
 * <pre>
 * Topology: ( [numNodes] Nodes, [numEdges] Edges )
 * Model ( [ModelNum] ):  [Model.toString()]
 *
 * Nodes: ([numNodes]):
 * [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
 * [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
 * [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
 * ...
 *
 * Edges: ([numEdges]):
 * [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]  [Direction]
 * [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]  [Direction]
 * [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]  [Direction]
 * ...
 * </pre>
 * <br>
*/

public class BriteExportNew {

    private Topology topology;
    private BufferedWriter bufferedWriter;
    private Graph graph;
    private String modelString;


}