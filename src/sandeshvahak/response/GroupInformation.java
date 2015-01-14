/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.response;

/**
 *
 * @author Vijay
 */
public class GroupInformation {
        
        private final String ID;
        private final String OWNER;
        private final long CREATION;
        private final String SUBJECT;
        private final long SUBJECTCHANGEDTIME;
        private final String SUBJECTCHANGEDBY;

        
        public String setId()
        {
            return this.ID;
        }
        public String getOwner()
        {
            return this.OWNER;
        }
        public long getCreation()
        {
            return this.CREATION;
        }
        public String getSubject()
        {
            return this.SUBJECT;
        }
        public long getSubjectChangedTime()
        {
            return this.SUBJECTCHANGEDTIME;
        }
        public String getSubjectChangedBy()
        {
            return this.SUBJECTCHANGEDBY;
        }
        
        

         public GroupInformation(String id, String owner, String creation, String subject, String subjectChanged, String subjectChangedBy)
        {
            this.ID = id;
            this.OWNER = owner;
            this.CREATION=Long.parseLong(creation);
            this.SUBJECT = subject;
            this.SUBJECTCHANGEDTIME=Long.parseLong(subjectChanged);
            this.SUBJECTCHANGEDBY = subjectChangedBy;
        }
}
