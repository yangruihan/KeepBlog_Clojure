(ns keepblog.models.migration
  (:require [clojure.java.jdbc :as sql]
            [keepblog.models.user :as user]))

(defn- migrated? [table-name]
  (-> (sql/query user/spec
                 [(str "SELECT COUNT(*) AS count FROM information_schema.tables "
                       "where table_name='" table-name "'")])
    first :count pos?))

(def user-table-name "users")

(defn- create-user-table []
  (when (not (migrated? user-table-name))
    (println (str "Creating table " user-table-name "...")) (flush)
    (sql/db-do-commands user/spec
                        (sql/create-table-ddl
                          user-table-name
                          [:id "INT PRIMARY KEY AUTO_INCREMENT"]
                          [:username "VARCHAR(50) NOT NULL"]
                          [:password "VARCHAR(50) NOT NULL"]
                          [:email "VARCHAR(255) NOT NULL"]
                          [:create_time "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"]))
    (println "Create Done!")))

(defn migrate []
  (do
    (create-user-table)))
                          