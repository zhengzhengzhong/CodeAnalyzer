package cn.letterme.code.analyzer;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

public class Searcher
{
    private Set<IMember> callerSet = new HashSet<IMember>();

    public Set<IMember> getCallers()
    {
        return callerSet;
    }

    public void search(IMember member)
    {
        try
        {
            IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
            // IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new
            // IJavaElement[] { (IJavaElement) project });
            SearchPattern searchParttern = SearchPattern.createPattern(member, IJavaSearchConstants.REFERENCES);
            SearchRequestor requestor = new SearchRequestor()
            {
                @Override
                public void acceptSearchMatch(SearchMatch match)
                {
                    if (match.getElement() instanceof IMember)
                    {
                        IMember caller = (IMember) match.getElement();
                        callerSet.add(caller);
                    }
                }
            };
            SearchEngine searchEngine = new SearchEngine();
            searchEngine.search(searchParttern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
                    scope, requestor, new NullProgressMonitor());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
