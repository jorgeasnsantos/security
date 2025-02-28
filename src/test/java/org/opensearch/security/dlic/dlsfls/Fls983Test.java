/*
 * Copyright OpenSearch Contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  You may not use this file except in compliance with the License.
 *  A copy of the License is located at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package org.opensearch.security.dlic.dlsfls;

import org.apache.http.HttpStatus;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.support.WriteRequest.RefreshPolicy;
import org.opensearch.client.Client;
import org.opensearch.common.xcontent.XContentType;
import org.junit.Assert;
import org.junit.Test;

import org.opensearch.security.test.DynamicSecurityConfig;
import org.opensearch.security.test.helper.rest.RestHelper.HttpResponse;

public class Fls983Test extends AbstractDlsFlsTest{


    protected void populateData(Client tc) {

        tc.index(new IndexRequest(".kibana").id("0").setRefreshPolicy(RefreshPolicy.IMMEDIATE)
                .source("{}", XContentType.JSON)).actionGet();
    }

    @Test
    public void test() throws Exception {

        setup(new DynamicSecurityConfig().setSecurityRoles("roles_983.yml"));

        HttpResponse res;

        String doc =  "{\"doc\" : {"+
            "\"x\" : \"y\""+
        "}}";

        Assert.assertEquals(HttpStatus.SC_OK, (res = rh.executePostRequest("/.kibana/_update/0?pretty", doc, encodeBasicHeader("human_resources_trainee", "password"))).getStatusCode());
        System.out.println(res.getBody());
        Assert.assertTrue(res.getBody().contains("updated"));
        Assert.assertTrue(res.getBody().contains("\"failed\" : 0"));
    }
}
