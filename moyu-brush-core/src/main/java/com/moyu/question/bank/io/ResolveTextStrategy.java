package com.moyu.question.bank.io;

import java.io.File;

public interface ResolveTextStrategy {

    String resolve(File file);

    boolean canResolve(File file);

}
