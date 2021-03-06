/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.epics.etherip.scan;

import static org.epics.etherip.EtherNetIP.logger;

import java.util.TimerTask;
import java.util.logging.Level;

import org.epics.etherip.Tag;
import org.epics.etherip.TagList;
import org.epics.etherip.protocol.Connection;

/** List of tags that are processed (read or written)
 *  @author Kay Kasemir
 */
class ScanList extends TimerTask
{
    final private double period;
    final private Connection connection;
    
    final private TagList tags = new TagList();
   
    private volatile boolean aborted = false;
    
    public ScanList(final double period, final Connection connection)
    {
        this.period = period;
        this.connection = connection;
    }

    public Tag add(final String tag_name)
    {
        return tags.add(tag_name);
    }
    
    @Override
    public void run()
    {
        logger.log(Level.FINE, "Scan list {0} sec", period);
        try
        {
            tags.process(connection);
        }
        catch (Exception ex)
        {
            if (aborted)
                return;
            logger.log(Level.WARNING, "Scan list " + period + " sec process failed", ex);
        }
    }
    
    @Override
    public boolean cancel()
    {
        aborted = true;
        return super.cancel();
    }
}