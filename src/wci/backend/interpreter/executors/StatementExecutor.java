package wci.backend.interpreter.executors;

import wci.backend.interpreter.*;
import wci.intermediate.*;
import wci.intermediate.icodeimpl.*;
import wci.message.*;

import static wci.backend.interpreter.RuntimeErrorCode.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;
import static wci.message.MessageType.*;

public class StatementExecutor extends Executor 
{
	
	public StatementExecutor(Executor parent) 
	{
		super(parent);
	}
	
	public Object execute(ICodeNode node)
	{
		ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();
		
		//	Send a message about the current source line.
		sendSourceLineMessage(node);
		
		// TODO this switch has a lot of code repetition that should be re-factored
		switch (nodeType)
		{
		
		case COMPOUND:{
			CompoundExecutor compoundExecutor = new CompoundExecutor(this);
			return compoundExecutor.execute(node);
		}
			
		case ASSIGN: {
			AssignmentExecutor assignmentExecutor = new AssignmentExecutor(this);
			return assignmentExecutor.execute(node);
		}
			
		case LOOP:{
			LoopExecutor loopExecutor = new LoopExecutor(this);
			return loopExecutor.execute(node);
		}
		case IF: {
			IfExecutor ifExecutor = new IfExecutor(this);
			return ifExecutor.execute(node);
		}
		case SELECT: {
			SelectExecutor selectExecutor = new SelectExecutor(this);
			return selectExecutor.execute(node);
		}
		case NO_OP: return null;
					
		default:
			{
				errorHandler.flag(node, UNIMPLEMENTED_FEATURE, this);
				return null;
			}
		}
	}
	
	private void sendSourceLineMessage(ICodeNode node)
	{
		Object lineNumber = node.getAttribute(LINE);
		
		//	Send the SOURCE_LINE message.
		if (lineNumber != null)
		{
			sendMessage(new Message(SOURCE_LINE, lineNumber));
		}
	}

}
