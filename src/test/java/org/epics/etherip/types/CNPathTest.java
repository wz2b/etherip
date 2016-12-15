/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.epics.etherip.types;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

import org.epics.etherip.util.Hexdump;

/** @author Kay Kasemir */
public class CNPathTest
{
	@Test
	public void testClassPath()
	{
		CNPath path = CNPath.Identity().instance(1).attr(0x7);
		System.out.println(path.toString());
		assertThat(path.toString(), equalTo("Path (3 el) Class(0x20) 0x1 (Identity), instance(0x24) 1, attrib.(0x30) 0x7"));
		
		final ByteBuffer buf = ByteBuffer.allocate(20);
		path.encode(buf, null);
		buf.flip();
		assertThat(Hexdump.toCompactHexdump(buf), equalTo("0000 - 03 20 01 24 01 30 07 - . .$.0."));
	}

	@Test
	public void testSimplePath() {
		final ByteBuffer buf = ByteBuffer.allocate(9);
		CNPath path = CNPath.Symbol("my_tag");

		path.encode(buf, null);


		final byte [] result = new byte[buf.position()];
		buf.flip();
		buf.get(result);

		final byte[] expected = {
				0x04, (byte) 0x91, 0x06, 0x6d, 0x79, 0x5f, 0x74, 0x61, 0x67
		};

		assertArrayEquals(expected, result);
	}


	@Test
	public void testSimplePathRequiresPadding() {
		final ByteBuffer buf = ByteBuffer.allocate(11);
		final CNPath path = CNPath.Symbol("my_tagg");

		path.encode(buf, null);


		final byte [] result = new byte[buf.position()];
		buf.flip();
		buf.get(result);

		byte[] expected = {
				0x05, (byte) 0x91, 0x07, 0x6d, 0x79, 0x5f, 0x74, 0x61, 0x67, 0x67, 0x00
		};

		assertArrayEquals(expected, result);
	}

	@Test
	public void testSimplePathWithDot()
	{
		final ByteBuffer buf = ByteBuffer.allocate(11);
		final CNPath path = CNPath.Symbol("test.p1");

		path.encode(buf, null);


		final byte [] result = new byte[buf.position()];
		buf.flip();
		buf.get(result);

		byte[] expected = {
				0x05, (byte) 0x91, 0x04, 0x74, 0x65, 0x73, 0x74, (byte) 0x91,  0x02, 0x70, 0x31
		};

		assertArrayEquals(expected, result);
	}

	@Test
	public void testSimplePathWithDotAndPadding()
	{
		final ByteBuffer buf = ByteBuffer.allocate(15);


		/*
		 * Two padding bytes should be added because the tag (test1) and attribute (p11)
		 * are both odd-length.
		 */

		final CNPath path = CNPath.Symbol("test1.p11");

		path.encode(buf, null);


		final byte [] result = new byte[buf.position()];
		buf.flip();
		buf.get(result);

		byte[] expected = {
				0x07, (byte) 0x91, 0x05, 0x74, 0x65, 0x73, 0x74, 0x31, 0x00,
				(byte) 0x91,  0x03, 0x70, 0x31, 0x31, 0x00
		};

		assertArrayEquals(expected, result);
	}

}
