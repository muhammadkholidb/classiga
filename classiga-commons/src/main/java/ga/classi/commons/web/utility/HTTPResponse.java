/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.web.utility;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import ga.classi.commons.utility.ActionResult;
import ga.classi.commons.constant.CommonConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;

@Slf4j
@Getter
public class HTTPResponse extends ActionResult {

    private String response;

    public HTTPResponse(String response) {
        this.response = response;
        try {
            JSONObject json = (JSONObject) JSONValue.parseWithException(response);
            this.status = (String) json.get(CommonConstants.STATUS);
            this.message = (String) json.get(CommonConstants.MESSAGE);
            this.data = (JSONObject) json.get(CommonConstants.DATA);
            parseData();
        } catch (ParseException e) {
            log.warn("Not a JSON formatted response, unable to parse"); 
        }
    }

    @Override
    public String toString() {
        return getResponse();
    }

}
