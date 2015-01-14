/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandeshvahak.parser;

/**
 *
 * @author Vijay
 */
public interface FMessageVisitor {
    void Audio(FMessage fMessage);

        void Contact(FMessage fMessage);

        void Image(FMessage fMessage);

        void Location(FMessage fMessage);

        void System(FMessage fMessage);

        void Undefined(FMessage fMessage);

        void Video(FMessage fMessage);
}
