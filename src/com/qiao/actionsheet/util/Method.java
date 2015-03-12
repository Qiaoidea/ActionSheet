
package com.qiao.actionsheet.util;

public class Method {
    public interface Action {
        void invoke();
    }

    public interface ActionE {
        void invoke() throws Exception;
    }

    public interface Action1<T1> {
        void invoke(T1 p);
    }

    public interface Action1E<T1> {
        void invoke(T1 p) throws Exception;
    }

    public interface Action2<T1, T2> {
        void invoke(T1 t1, T2 t2);
    }

    public interface Action3<T1, T2, T3> {
        void invoke(T1 t1, T2 t2, T3 t3);
    }

    public interface Func<Tresult> {
        Tresult invoke();
    }

    public interface Func1<T1, Tresult> {
        Tresult invoke(T1 t1);
    }

    public interface Func2<T1, T2, Tresult> {
        Tresult invoke(T1 t1, T2 t2);
    }
}
