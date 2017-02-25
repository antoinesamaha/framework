package com.foc.tree;

public interface INodeItertorFilter<N extends FNode>{
	public boolean includeNode(N node);
}
