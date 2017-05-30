package cn.letterme.code.analyzer.param;

import org.eclipse.core.resources.IMarker;

public class DeadCodeMarkerParam
{
    private int start;
    private int end;
    private String file;
    private String message;
    private int priority = IMarker.PRIORITY_HIGH;
    private int severity = IMarker.SEVERITY_ERROR;

    public int getStart()
    {
        return start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public int getEnd()
    {
        return end;
    }

    public void setEnd(int end)
    {
        this.end = end;
    }

    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file = file;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getPriority()
    {
        return priority;
    }

    public int getSeverity()
    {
        return severity;
    }

    @Override
    public String toString()
    {
        return "UnusedCodeMarkerParam [start=" + start + ", end=" + end + ", message=" + message + "]";
    }
}
