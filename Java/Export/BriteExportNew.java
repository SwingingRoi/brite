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
        try {
         writeTitle();
         writeNodes();
         writeEdges();
        }catch(Exception e) {
            System.out.println("[BRITE ERROR]: Error exporting to file. " + e);
            e.printStackTrace();
            System.exit(0);

        }
        Util.MSG("... DONE.");
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
                if (specificNodeType == ModelConstants.AS_LEAF) bufferedWriter.write("\tAS_LEAF");
                else if (specificNodeType == ModelConstants.AS_BORDER) bufferedWriter.write("\tAS_BORDER");
                else if (specificNodeType == ModelConstants.AS_STUB) bufferedWriter.write("\tAS_STUB");
                else if (specificNodeType == ModelConstants.AS_BACKBONE) bufferedWriter.write("\tAS_BACKBONE");
                else  bufferedWriter.write("\tAS_NODE");
            }
            bufferedWriter.newLine();
          }
          bufferedWriter.newLine();
          bufferedWriter.newLine();
    }

    public void writeEdges() throws IOException
    {
        /*output edges*/
       Edge[] edges = graph.getEdgesArray();
       bufferedWriter.write("Edges: ( " + edges.length + " )");
       bufferedWriter.newLine();

       Arrays.sort(edges, Edge.IDcomparator);
       for (int i = 0; i < edges.length; ++i) {
           Edge e = (Edge) edges[i];
           Node src = e.getSrc();
           Node dst = e.getDst();
           double dist = e.getEuclideanDist();
           double delay = e.getDelay();
           int asFrom = src.getID();
           int asTo = dst.getID();
           if (src.getNodeConf() instanceof RouterNodeConf)
               asFrom = ((RouterNodeConf) src.getNodeConf()).getCorrAS();
           if (dst.getNodeConf() instanceof RouterNodeConf)
               asTo = ((RouterNodeConf) dst.getNodeConf()).getCorrAS();

           bufferedWriter.write(e.getID() + "\t" + src.getID() + "\t" + dst.getID());
           bufferedWriter.write("\t" + dist + "\t" + delay + "\t" + e.getbufferedWriter());
           bufferedWriter.write("\t" + asFrom + "\t" + asTo);


           if (e.getEdgeConf() instanceof ASEdgeConf) {
               int specificEdgeType = ((ASEdgeConf) e.getEdgeConf()).getType();
               if (specificEdgeType == ModelConstants.E_AS_STUB)
                   bufferedWriter.write("\tE_AS_STUB");
               else if (specificEdgeType == ModelConstants.E_AS_BORDER)
                   bufferedWriter.write("\tE_AS_BORDER");
               else if (specificEdgeType == ModelConstants.E_AS_BACKBONE)/*backbone*/
                   bufferedWriter.write("\tE_AS_BACKBONE_LINK");
               else
                   bufferedWriter.write("\tE_AS");
           } else  /*we have a router*/ {
               int specificEdgeType = ((RouterEdgeConf) e.getEdgeConf()).getType();
               if (specificEdgeType == ModelConstants.E_RT_STUB)
                   bufferedWriter.write("\tE_RT_STUB");
               else if (specificEdgeType == ModelConstants.E_RT_BORDER)
                   bufferedWriter.write("\tE_RT_BORDER");
               else if (specificEdgeType == ModelConstants.E_RT_BACKBONE)/*backbone*/
                   bufferedWriter.write("\tE_RT_BACKBONE");
               else
                   bufferedWriter.write("\tE_RT");
           }

           if (e.getDirection() == GraphConstants.DIRECTED)
               bufferedWriter.write("\tD");
           else bufferedWriter.write("\tU");

           bufferedWriter.newLine();

       }
       bufferedWriter.close();
    }
} 