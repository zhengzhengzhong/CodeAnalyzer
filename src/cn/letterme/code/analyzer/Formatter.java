package cn.letterme.code.analyzer;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

public class Formatter
{
    public static String format(IMethod method) throws JavaModelException
    {
        String signatureOfMethod = Signature.toString(method.getSignature(), method.getElementName(),
                method.getParameterNames(), false, true);
        return format(method.getDeclaringType()) + ":" + signatureOfMethod;
    }
    
    public static String format(IType type)
    {
        return type.getFullyQualifiedName();
    }

    public static String format(IField field) throws JavaModelException
    {
        return format(field.getDeclaringType()) + ":" + field.getTypeSignature() + ">" + field.getElementName();
    }
}
