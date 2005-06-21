package seg.jUCMNav.figures;

import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Rectangle;

import ucm.map.NodeConnection;

/**
 * Created 2005-03-02
 * 
 * This figure represent a connection between two path nodes.
 * 
 * @author Etienne Tremblay
 */
public class SplineConnection extends PolylineConnection {
    /**
     * This figure contains a final reference to the link it represent because the connection router needs this reference. The link is never modified since it's
     * final.
     */
    private final NodeConnection link;

    public SplineConnection(NodeConnection link) {
        super();
        this.link = link;
    }

    /**
     * @return Returns the link.
     */
    public NodeConnection getLink() {
        return link;
    }

    /**
     * Lays out this Figure using its {@link LayoutManager}.
     * 
     * @since 2.0
     */
    public void layout() {
        if (getSourceAnchor() != null && getTargetAnchor() != null)
            getConnectionRouter().route(this);

        Rectangle oldBounds = bounds;
        if (this.getPoints().size() > 0)
            super.layout();
        bounds = null;

        if (!getBounds().contains(oldBounds)) {
            getParent().translateToParent(oldBounds);
            getUpdateManager().addDirtyRegion(getParent(), oldBounds);
        }

        repaint();
        fireMoved();
    }
}