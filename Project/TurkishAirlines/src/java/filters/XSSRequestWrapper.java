package filters;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

    private static Pattern[] patterns = new Pattern[]{
        // Script fragments
        Pattern.compile("<script>(.*?)</script>", 0x02),
        // src='...'
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
        // lonely script tags
        Pattern.compile("</script>", 0x02),
        Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
        // eval(...)
        Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
        // expression(...)
        Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
        // javascript:...
        Pattern.compile("javascript:", 0x02),
        // vbscript:...
        Pattern.compile("vbscript:", 0x02),
        // onload(...)=...
        Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
    };

    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        return stripXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }

    private String stripXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
    
    /*
    private static Pattern[] patterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", 0x02),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
            // lonely script tags
            Pattern.compile("</script>", 0x02),
            Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
            // javascript:...
            Pattern.compile("javascript:", 0x02),
            // vbscript:...
            Pattern.compile("vbscript:", 0x02),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
        };

        public XSSRequestWrapper(HttpServletRequest servletRequest) {
            super(servletRequest);
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);

            if (values == null) {
                return null;
            }

            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = stripXSS(values[i]);
            }

            return encodedValues;
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);

            return stripXSS(value);
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return stripXSS(value);
        }

        private String stripXSS(String value) {
            if (value != null) {
                // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                // avoid encoded attacks.
                // value = ESAPI.encoder().canonicalize(value);

                // Avoid null characters
                value = value.replaceAll("\0", "");

                // Remove all sections that match a pattern
                for (Pattern scriptPattern : patterns){
                    value = scriptPattern.matcher(value).replaceAll("");
                }
            }
            return value;
        }
        
        private static Pattern[] patterns = new Pattern[]{
                // Script fragments
                Pattern.compile("<script>(.*?)</script>", 0x02),
                // src='...'
                Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                // lonely script tags
                Pattern.compile("</script>", 0x02),
                Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                // eval(...)
                Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                // expression(...)
                Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                // javascript:...
                Pattern.compile("javascript:", 0x02),
                // vbscript:...
                Pattern.compile("vbscript:", 0x02),
                // onload(...)=...
                Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
            };

            public XSSRequestWrapper(HttpServletRequest servletRequest) {
                super(servletRequest);
            }

            @Override
            public String[] getParameterValues(String parameter) {
                String[] values = super.getParameterValues(parameter);

                if (values == null) {
                    return null;
                }

                int count = values.length;
                String[] encodedValues = new String[count];
                for (int i = 0; i < count; i++) {
                    encodedValues[i] = stripXSS(values[i]);
                }

                return encodedValues;
            }

            @Override
            public String getParameter(String parameter) {
                String value = super.getParameter(parameter);

                return stripXSS(value);
            }

            @Override
            public String getHeader(String name) {
                String value = super.getHeader(name);
                return stripXSS(value);
            }

            private String stripXSS(String value) {
                if (value != null) {
                    // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                    // avoid encoded attacks.
                    // value = ESAPI.encoder().canonicalize(value);

                    // Avoid null characters
                    value = value.replaceAll("\0", "");

                    // Remove all sections that match a pattern
                    for (Pattern scriptPattern : patterns){
                        value = scriptPattern.matcher(value).replaceAll("");
                    }
                }
                return value;
            }
            
            
            private static Pattern[] patterns = new Pattern[]{
                    // Script fragments
                    Pattern.compile("<script>(.*?)</script>", 0x02),
                    // src='...'
                    Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                    Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                    // lonely script tags
                    Pattern.compile("</script>", 0x02),
                    Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                    // eval(...)
                    Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                    // expression(...)
                    Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                    // javascript:...
                    Pattern.compile("javascript:", 0x02),
                    // vbscript:...
                    Pattern.compile("vbscript:", 0x02),
                    // onload(...)=...
                    Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
                };

                public XSSRequestWrapper(HttpServletRequest servletRequest) {
                    super(servletRequest);
                }

                @Override
                public String[] getParameterValues(String parameter) {
                    String[] values = super.getParameterValues(parameter);

                    if (values == null) {
                        return null;
                    }

                    int count = values.length;
                    String[] encodedValues = new String[count];
                    for (int i = 0; i < count; i++) {
                        encodedValues[i] = stripXSS(values[i]);
                    }

                    return encodedValues;
                }

                @Override
                public String getParameter(String parameter) {
                    String value = super.getParameter(parameter);

                    return stripXSS(value);
                }

                @Override
                public String getHeader(String name) {
                    String value = super.getHeader(name);
                    return stripXSS(value);
                }

                private String stripXSS(String value) {
                    if (value != null) {
                        // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                        // avoid encoded attacks.
                        // value = ESAPI.encoder().canonicalize(value);

                        // Avoid null characters
                        value = value.replaceAll("\0", "");

                        // Remove all sections that match a pattern
                        for (Pattern scriptPattern : patterns){
                            value = scriptPattern.matcher(value).replaceAll("");
                        }
                    }
                    return value;
                }
                
                
                private static Pattern[] patterns = new Pattern[]{
                        // Script fragments
                        Pattern.compile("<script>(.*?)</script>", 0x02),
                        // src='...'
                        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                        // lonely script tags
                        Pattern.compile("</script>", 0x02),
                        Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                        // eval(...)
                        Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                        // expression(...)
                        Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                        // javascript:...
                        Pattern.compile("javascript:", 0x02),
                        // vbscript:...
                        Pattern.compile("vbscript:", 0x02),
                        // onload(...)=...
                        Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
                    };

                    public XSSRequestWrapper(HttpServletRequest servletRequest) {
                        super(servletRequest);
                    }

                    @Override
                    public String[] getParameterValues(String parameter) {
                        String[] values = super.getParameterValues(parameter);

                        if (values == null) {
                            return null;
                        }

                        int count = values.length;
                        String[] encodedValues = new String[count];
                        for (int i = 0; i < count; i++) {
                            encodedValues[i] = stripXSS(values[i]);
                        }

                        return encodedValues;
                    }

                    @Override
                    public String getParameter(String parameter) {
                        String value = super.getParameter(parameter);

                        return stripXSS(value);
                    }

                    @Override
                    public String getHeader(String name) {
                        String value = super.getHeader(name);
                        return stripXSS(value);
                    }

                    private String stripXSS(String value) {
                        if (value != null) {
                            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                            // avoid encoded attacks.
                            // value = ESAPI.encoder().canonicalize(value);

                            // Avoid null characters
                            value = value.replaceAll("\0", "");

                            // Remove all sections that match a pattern
                            for (Pattern scriptPattern : patterns){
                                value = scriptPattern.matcher(value).replaceAll("");
                            }
                        }
                        return value;
                    }
                    
                    private static Pattern[] patterns = new Pattern[]{
                            // Script fragments
                            Pattern.compile("<script>(.*?)</script>", 0x02),
                            // src='...'
                            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                            // lonely script tags
                            Pattern.compile("</script>", 0x02),
                            Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                            // eval(...)
                            Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                            // expression(...)
                            Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                            // javascript:...
                            Pattern.compile("javascript:", 0x02),
                            // vbscript:...
                            Pattern.compile("vbscript:", 0x02),
                            // onload(...)=...
                            Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
                        };

                        public XSSRequestWrapper(HttpServletRequest servletRequest) {
                            super(servletRequest);
                        }

                        @Override
                        public String[] getParameterValues(String parameter) {
                            String[] values = super.getParameterValues(parameter);

                            if (values == null) {
                                return null;
                            }

                            int count = values.length;
                            String[] encodedValues = new String[count];
                            for (int i = 0; i < count; i++) {
                                encodedValues[i] = stripXSS(values[i]);
                            }

                            return encodedValues;
                        }

                        @Override
                        public String getParameter(String parameter) {
                            String value = super.getParameter(parameter);

                            return stripXSS(value);
                        }

                        @Override
                        public String getHeader(String name) {
                            String value = super.getHeader(name);
                            return stripXSS(value);
                        }

                        private String stripXSS(String value) {
                            if (value != null) {
                                // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                                // avoid encoded attacks.
                                // value = ESAPI.encoder().canonicalize(value);

                                // Avoid null characters
                                value = value.replaceAll("\0", "");

                                // Remove all sections that match a pattern
                                for (Pattern scriptPattern : patterns){
                                    value = scriptPattern.matcher(value).replaceAll("");
                                }
                            }
                            return value;
                        }
                        private static Pattern[] patterns = new Pattern[]{
                                // Script fragments
                                Pattern.compile("<script>(.*?)</script>", 0x02),
                                // src='...'
                                Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                                Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                                // lonely script tags
                                Pattern.compile("</script>", 0x02),
                                Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                                // eval(...)
                                Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                // expression(...)
                                Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                // javascript:...
                                Pattern.compile("javascript:", 0x02),
                                // vbscript:...
                                Pattern.compile("vbscript:", 0x02),
                                // onload(...)=...
                                Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
                            };

                            public XSSRequestWrapper(HttpServletRequest servletRequest) {
                                super(servletRequest);
                            }

                            @Override
                            public String[] getParameterValues(String parameter) {
                                String[] values = super.getParameterValues(parameter);

                                if (values == null) {
                                    return null;
                                }

                                int count = values.length;
                                String[] encodedValues = new String[count];
                                for (int i = 0; i < count; i++) {
                                    encodedValues[i] = stripXSS(values[i]);
                                }

                                return encodedValues;
                            }

                            @Override
                            public String getParameter(String parameter) {
                                String value = super.getParameter(parameter);

                                return stripXSS(value);
                            }

                            @Override
                            public String getHeader(String name) {
                                String value = super.getHeader(name);
                                return stripXSS(value);
                            }

                            private String stripXSS(String value) {
                                if (value != null) {
                                    // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                                    // avoid encoded attacks.
                                    // value = ESAPI.encoder().canonicalize(value);

                                    // Avoid null characters
                                    value = value.replaceAll("\0", "");

                                    // Remove all sections that match a pattern
                                    for (Pattern scriptPattern : patterns){
                                        value = scriptPattern.matcher(value).replaceAll("");
                                    }
                                }
                                return value;
                            }
                            private static Pattern[] patterns = new Pattern[]{
                                    // Script fragments
                                    Pattern.compile("<script>(.*?)</script>", 0x02),
                                    // src='...'
                                    Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                                    Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                                    // lonely script tags
                                    Pattern.compile("</script>", 0x02),
                                    Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                                    // eval(...)
                                    Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                    // expression(...)
                                    Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                    // javascript:...
                                    Pattern.compile("javascript:", 0x02),
                                    // vbscript:...
                                    Pattern.compile("vbscript:", 0x02),
                                    // onload(...)=...
                                    Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
                                };

                                public XSSRequestWrapper(HttpServletRequest servletRequest) {
                                    super(servletRequest);
                                }

                                @Override
                                public String[] getParameterValues(String parameter) {
                                    String[] values = super.getParameterValues(parameter);

                                    if (values == null) {
                                        return null;
                                    }

                                    int count = values.length;
                                    String[] encodedValues = new String[count];
                                    for (int i = 0; i < count; i++) {
                                        encodedValues[i] = stripXSS(values[i]);
                                    }

                                    return encodedValues;
                                }

                                @Override
                                public String getParameter(String parameter) {
                                    String value = super.getParameter(parameter);

                                    return stripXSS(value);
                                }

                                @Override
                                public String getHeader(String name) {
                                    String value = super.getHeader(name);
                                    return stripXSS(value);
                                }

                                private String stripXSS(String value) {
                                    if (value != null) {
                                        // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                                        // avoid encoded attacks.
                                        // value = ESAPI.encoder().canonicalize(value);

                                        // Avoid null characters
                                        value = value.replaceAll("\0", "");

                                        // Remove all sections that match a pattern
                                        for (Pattern scriptPattern : patterns){
                                            value = scriptPattern.matcher(value).replaceAll("");
                                        }
                                    }
                                    return value;
                                }
                                private static Pattern[] patterns = new Pattern[]{
                                        // Script fragments
                                        Pattern.compile("<script>(.*?)</script>", 0x02),
                                        // src='...'
                                        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                                        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                                        // lonely script tags
                                        Pattern.compile("</script>", 0x02),
                                        Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                                        // eval(...)
                                        Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                        // expression(...)
                                        Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                        // javascript:...
                                        Pattern.compile("javascript:", 0x02),
                                        // vbscript:...
                                        Pattern.compile("vbscript:", 0x02),
                                        // onload(...)=...
                                        Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
                                    };

                                    public XSSRequestWrapper(HttpServletRequest servletRequest) {
                                        super(servletRequest);
                                    }

                                    @Override
                                    public String[] getParameterValues(String parameter) {
                                        String[] values = super.getParameterValues(parameter);

                                        if (values == null) {
                                            return null;
                                        }

                                        int count = values.length;
                                        String[] encodedValues = new String[count];
                                        for (int i = 0; i < count; i++) {
                                            encodedValues[i] = stripXSS(values[i]);
                                        }

                                        return encodedValues;
                                    }

                                    @Override
                                    public String getParameter(String parameter) {
                                        String value = super.getParameter(parameter);

                                        return stripXSS(value);
                                    }

                                    @Override
                                    public String getHeader(String name) {
                                        String value = super.getHeader(name);
                                        return stripXSS(value);
                                    }

                                    private String stripXSS(String value) {
                                        if (value != null) {
                                            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                                            // avoid encoded attacks.
                                            // value = ESAPI.encoder().canonicalize(value);

                                            // Avoid null characters
                                            value = value.replaceAll("\0", "");

                                            // Remove all sections that match a pattern
                                            for (Pattern scriptPattern : patterns){
                                                value = scriptPattern.matcher(value).replaceAll("");
                                            }
                                        }
                                        return value;
                                    }
                                    private static Pattern[] patterns = new Pattern[]{
                                            // Script fragments
                                            Pattern.compile("<script>(.*?)</script>", 0x02),
                                            // src='...'
                                            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                                            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                                            // lonely script tags
                                            Pattern.compile("</script>", 0x02),
                                            Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                                            // eval(...)
                                            Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                            // expression(...)
                                            Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                            // javascript:...
                                            Pattern.compile("javascript:", 0x02),
                                            // vbscript:...
                                            Pattern.compile("vbscript:", 0x02),
                                            // onload(...)=...
                                            Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
                                        };

                                        public XSSRequestWrapper(HttpServletRequest servletRequest) {
                                            super(servletRequest);
                                        }

                                        @Override
                                        public String[] getParameterValues(String parameter) {
                                            String[] values = super.getParameterValues(parameter);

                                            if (values == null) {
                                                return null;
                                            }

                                            int count = values.length;
                                            String[] encodedValues = new String[count];
                                            for (int i = 0; i < count; i++) {
                                                encodedValues[i] = stripXSS(values[i]);
                                            }

                                            return encodedValues;
                                        }

                                        @Override
                                        public String getParameter(String parameter) {
                                            String value = super.getParameter(parameter);

                                            return stripXSS(value);
                                        }

                                        @Override
                                        public String getHeader(String name) {
                                            String value = super.getHeader(name);
                                            return stripXSS(value);
                                        }

                                        private String stripXSS(String value) {
                                            if (value != null) {
                                                // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                                                // avoid encoded attacks.
                                                // value = ESAPI.encoder().canonicalize(value);

                                                // Avoid null characters
                                                value = value.replaceAll("\0", "");

                                                // Remove all sections that match a pattern
                                                for (Pattern scriptPattern : patterns){
                                                    value = scriptPattern.matcher(value).replaceAll("");
                                                }
                                            }
                                            return value;
                                        }
                                        private static Pattern[] patterns = new Pattern[]{
                                                // Script fragments
                                                Pattern.compile("<script>(.*?)</script>", 0x02),
                                                // src='...'
                                                Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 0x02 | 0x08 | 0x20),
                                                Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 0x02 | 0x08 | 0x20),
                                                // lonely script tags
                                                Pattern.compile("</script>", 0x02),
                                                Pattern.compile("<script(.*?)>", 0x02 | 0x08 | 0x20),
                                                // eval(...)
                                                Pattern.compile("eval\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                                // expression(...)
                                                Pattern.compile("expression\\((.*?)\\)", 0x02 | 0x08 | 0x20),
                                                // javascript:...
                                                Pattern.compile("javascript:", 0x02),
                                                // vbscript:...
                                                Pattern.compile("vbscript:", 0x02),
                                                // onload(...)=...
                                                Pattern.compile("onload(.*?)=", 0x02 | 0x08 | 0x20)
                                            };

                                            public XSSRequestWrapper(HttpServletRequest servletRequest) {
                                                super(servletRequest);
                                            }

                                            @Override
                                            public String[] getParameterValues(String parameter) {
                                                String[] values = super.getParameterValues(parameter);

                                                if (values == null) {
                                                    return null;
                                                }

                                                int count = values.length;
                                                String[] encodedValues = new String[count];
                                                for (int i = 0; i < count; i++) {
                                                    encodedValues[i] = stripXSS(values[i]);
                                                }

                                                return encodedValues;
                                            }

                                            @Override
                                            public String getParameter(String parameter) {
                                                String value = super.getParameter(parameter);

                                                return stripXSS(value);
                                            }

                                            @Override
                                            public String getHeader(String name) {
                                                String value = super.getHeader(name);
                                                return stripXSS(value);
                                            }

                                            private String stripXSS(String value) {
                                                if (value != null) {
                                                    // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
                                                    // avoid encoded attacks.
                                                    // value = ESAPI.encoder().canonicalize(value);

                                                    // Avoid null characters
                                                    value = value.replaceAll("\0", "");

                                                    // Remove all sections that match a pattern
                                                    for (Pattern scriptPattern : patterns){
                                                        value = scriptPattern.matcher(value).replaceAll("");
                                                    }
                                                }
                                                return value;
                                            }*/
}