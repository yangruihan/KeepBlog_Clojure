(defproject keepblog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [mysql/mysql-connector-java "5.1.25"]
                 [javax.servlet/servlet-api "2.5"]
                 [korma "0.4.2"]
                 [ring "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.4.0"]
                 [selmer "1.0.0"]])
