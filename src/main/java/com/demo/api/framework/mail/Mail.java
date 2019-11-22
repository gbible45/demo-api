package com.demo.api.framework.mail;

import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.util.XpertUtil;

public class Mail {

    public Mail() {}

    public String sendMail(String message, String verifyLink) {
        String mail =
        "<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"\">\n" +
           "<colgroup>\n" +
                "<col width=\"25\" />\n" +
                "<col width=\"550\" />\n" +
                "<col width=\"25\" />\n" +
           "</colgroup>\n" +
            "<tbody>\n" +
                "<tr>\n" +
                "<td colspan=\"3\" height=\"25\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td width=\"25\"></td>\n" +
                    "<td width=\"550\" height=\"23\">\n" +
                        "<img src=\"https://s3.amazonaws.com/mail-dev/mail_logo.gif\" style=\"vertical-align:top;\" />\n" +
                        "<strong style=\"display:inline-block; padding-left:10px; vertical-align:top; color:#000; font-size:16px; line-height:22px;\"></strong>\n" +
                    "</td>\n" +
                    "<td width=\"25\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"30\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td></td>\n" +
                    "<td height=\"220\" style=\"text-align:center;\">\n" + "<img src=\"https://s3.amazonaws.com/mail-dev/mail_bg.gif\" style=\"vertical-align:top;\" />\n" + "</td>\n" +
                    "<td></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"35\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td></td>\n" +
                    "<td height=\"40\" style=\"color:#000; font-size:18px; font-weight:bold; text-align:center;\">"+ XpertUtil.getMessage("com.naturalprogrammer.spring.greet")+"</td>\n" +
                    "<td></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td></td>\n" +
                    "<td style=\"color:#000; font-size:16px; text-align:center;\">"+message+"</td>\n" +
                    "<td></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"30\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td></td>\n" +
                    "<td style=\"background:#efefef;\">\n" +
                    "<a href=\""+verifyLink+"\" style=\"display:block; padding:15px; font-family:'dotum','돋움'; color:#346eff; font-size:14px; vertical-align:top; text-decoration:none;\">"+verifyLink+"\n</a>\n" +
                    "</td>\n" +
                    "<td></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"10\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td></td>\n" +
                    "<td style=\"color:#000; font-size:12px; text-align:right;\">Copyright © PaaSXpert All rights reserved.</td>\n" +
                    "<td></td>\n" +
                "</tr>\n" +
            "</tbody>\n" +
        "</table>";

        return mail;
    }

    public String sendQnAMail(String message) {
        String mail =
        "<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"\">\n" +
            "<colgroup>\n" +
                "<col width=\"25\" />\n" +
                "<col width=\"550\" />\n" +
                "<col width=\"25\" />\n" +
            "</colgroup>\n" +
            "<tbody>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"25\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td width=\"25\"></td>\n" +
                    "<td width=\"550\" height=\"23\">\n" +
                        "<img src=\"https://s3.amazonaws.com/mail-dev/mail_logo.gif\" style=\"vertical-align:top;\" />\n" +
                        "<strong style=\"display:inline-block; padding-left:10px; vertical-align:top; color:#000; font-size:16px; line-height:22px;\">QnA 메일발송</strong>\n" +
                    "</td>\n" +
                    "<td width=\"25\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"30\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td></td>\n" +
                    "<td height=\"220\" style=\"text-align:center;\">\n" + "<img src=\"https://s3.amazonaws.com/mail-dev/mail_bg.gif\" style=\"vertical-align:top;\" />\n" + "</td>\n" +
                    "<td></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"35\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td></td>\n" +
                    "<td style=\"color:#000; font-size:16px; text-align:center;\">"+message+"</td>\n" +
                    "<td></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"30\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td colspan=\"3\" height=\"10\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                    "<td></td>\n" +
                    "<td style=\"color:#000; font-size:12px; text-align:right;\">Copyright © PaaSXpert All rights reserved.</td>\n" +
                    "<td></td>\n" +
                "</tr>\n" +
            "</tbody>\n" +
        "</table>";

        return mail;
    }
}
