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

    /**
     * Class Constructor: Returns a BriteExport object which your code
     * my keep around.  Does not actually write the topology to the
     * file.  You must explicitly call the <code>export()</code> method of this
     * object in order to write to the file.
     *
     * @param t       the topology object to export
     * @param outFile the destination file to write the topology to.
     */
    public BriteExportNew(Topology topology, File outFile) 
    {
        this.topology = topology;

        try
        {
            bufferedWriter = new  BufferedWriter(new FileWriter(outFile));
        }catch(IOException e)
        {
            Util.ERR("Error creating BufferedWriter in BriteExport:"+e);
        }
        graph = topology.getGraph();
        modelString = topology.getModel().toString();
    }
      
      /**
       * Writes the contents of the topolgy in the BRITE format to the destination
       * file specified in the constructor.
       * 
       * @throws IOException
       */
    public void export() throws IOException
    {
        Util.MSG("Exporting to BRITE");
        writeTitle();
        
    }
    // write the title of brite
    public void writeTitle() throws IOException
    {
        //write the edges and the nodes
        bufferedWriter.write("Topology: ( " + graph.getNumNodes() + " Nodes, " + graph.getNumEdges() + " Edges )");
        bufferedWriter.newLine();
        //write the mode type
        bufferedWriter.write(modelString);
        bufferedWriter.newLine();
    }

   //write the node information 
    public void writeNodes() throws IOException
    {
          Node[] nodes = graph.getNodesArray();
          Arrays.sort(nodes, Node.IDcomparator);
          
          for(int i = 0; i < nodes.length ; ++i )
          {
              Node node = nodes[i];
              int x = (int)((NodeConf)node.getNodeConf()).getX();
              int y = (int)((NodeConf)node.getNodeConf()).getY();
              int specificNodeType = -1;
              int ASid = -1;
              int inDegree =node.getInDegree();
              int outDegree =node.getOutDegree();
              int nodeID = node.getID(); 

            if (node.getNodeConf() instanceof RouterNodeConf) {
                ASid = ((RouterNodeConf) node.getNodeConf()).getCorrAS();
                specificNodeType = ((RouterNodeConf) node.getNodeConf()).getType();
            }
            if (node.getNodeConf() instanceof ASNodeConf) {
                specificNodeType = ((ASNodeConf) node.getNodeConf()).getType();
                ASid = nodeID;
            }
            bufferedWriter.write(nodeID + "\t" + x +"\t"+y+"\t"+inDegree+"\t"+outDegree+"\t"+ASid);
            
            if(node.getNodeConf() instanceof RouterNodeConf)
            {
                if(specificNodeType == ModelConstants.RT_LEAF) bufferedWriter.write("\tRT_LEAF");
                else if (specificNodeType == ModelConstants.RT_BORDER) bufferedWriter.write("\tRT_BORDER");
                else if (specificNodeType == ModelConstants.RT_STUB) bufferedWriter.write("\tRT_STUB");
                else if (specificNodeType == ModelConstants.RT_BACKBONE) bufferedWriter.write("\tRT_BACKBONE");
                else bufferedWriter.write("\tRT_NODE");              
            }
            else if (node.getNodeConf() instanceof ASNodeConf)
            {
                
            }
          }
    }
} 