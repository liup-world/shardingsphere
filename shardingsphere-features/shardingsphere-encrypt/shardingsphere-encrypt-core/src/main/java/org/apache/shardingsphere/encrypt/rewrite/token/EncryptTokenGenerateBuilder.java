/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.encrypt.rewrite.token;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.encrypt.rewrite.aware.QueryWithCipherColumnAware;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.AssistQueryAndPlainInsertColumnsTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptAlterTableTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptAssignmentTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptCreateTableTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptForUseDefaultInsertColumnsTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptInsertOnUpdateTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptInsertValuesTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptPredicateColumnTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptPredicateRightValueTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.EncryptProjectionTokenGenerator;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.impl.InsertCipherNameTokenGenerator;
import org.apache.shardingsphere.encrypt.rule.EncryptRule;
import org.apache.shardingsphere.encrypt.rule.aware.EncryptRuleAware;
import org.apache.shardingsphere.infra.rewrite.sql.token.generator.SQLTokenGenerator;
import org.apache.shardingsphere.infra.rewrite.sql.token.generator.builder.SQLTokenGeneratorBuilder;

import java.util.Collection;
import java.util.LinkedList;

/**
 * SQL token generator builder for encrypt.
 */
@RequiredArgsConstructor
public final class EncryptTokenGenerateBuilder implements SQLTokenGeneratorBuilder {
    
    private final EncryptRule encryptRule;
    
    private final boolean queryWithCipherColumn;
    
    @Override
    public Collection<SQLTokenGenerator> getSQLTokenGenerators() {
        Collection<SQLTokenGenerator> result = new LinkedList<>();
        addSQLTokenGenerator(result, new EncryptProjectionTokenGenerator());
        addSQLTokenGenerator(result, new EncryptAssignmentTokenGenerator());
        addSQLTokenGenerator(result, new EncryptPredicateColumnTokenGenerator());
        addSQLTokenGenerator(result, new EncryptPredicateRightValueTokenGenerator());
        addSQLTokenGenerator(result, new EncryptInsertValuesTokenGenerator());
        addSQLTokenGenerator(result, new EncryptForUseDefaultInsertColumnsTokenGenerator());
        addSQLTokenGenerator(result, new InsertCipherNameTokenGenerator());
        addSQLTokenGenerator(result, new AssistQueryAndPlainInsertColumnsTokenGenerator());
        addSQLTokenGenerator(result, new EncryptInsertOnUpdateTokenGenerator());
        addSQLTokenGenerator(result, new EncryptCreateTableTokenGenerator());
        addSQLTokenGenerator(result, new EncryptAlterTableTokenGenerator());
        return result;
    }
    
    private void addSQLTokenGenerator(final Collection<SQLTokenGenerator> sqlTokenGenerators, final SQLTokenGenerator toBeAddedSQLTokenGenerator) {
        if (toBeAddedSQLTokenGenerator instanceof EncryptRuleAware) {
            ((EncryptRuleAware) toBeAddedSQLTokenGenerator).setEncryptRule(encryptRule);
        }
        if (toBeAddedSQLTokenGenerator instanceof QueryWithCipherColumnAware) {
            ((QueryWithCipherColumnAware) toBeAddedSQLTokenGenerator).setQueryWithCipherColumn(queryWithCipherColumn);
        }
        sqlTokenGenerators.add(toBeAddedSQLTokenGenerator);
    }
}
