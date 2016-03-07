(ns keepblog.web
  (:use [noir.validation :only (wrap-noir-validation)]
		    [noir.cookies :only (wrap-noir-cookies)]
		    [noir.session :only (mem wrap-noir-session wrap-noir-flash)]
        [ring.middleware.session.memory :only (memory-store)]
        [ring.middleware.anti-forgery])
  (:require [ring.adapter.jetty :as ring]
				    [ring.middleware.params :as params]
				    [ring.middleware.keyword-params :as keyword-params]
				    [ring.middleware.json :as json]
				    [ring.middleware.resource :as resource]
				    [ring.middleware.stacktrace :as stacktrace]
		        [noir.util.middleware :as noir]
		        [keepblog.middleware.templating :as templating]
		        [keepblog.middleware.urlhandlers :as urlhandlers]
	          [keepblog.models.migration :as schema]))

(def app
  (-> urlhandlers/app-routes
    (resource/wrap-resource (clojure.java.io/resource "resources"))  ;; static resource
    wrap-anti-forgery
    noir/wrap-request-map
    templating/wrap-template-response  ;; render template
    json/wrap-json-response            ;; render json
    json/wrap-json-body                ;; request json
    stacktrace/wrap-stacktrace-web     ;; wrap-stacktrace-log
    keyword-params/wrap-keyword-params ;; convert parameter name to keyword
    params/wrap-params                 ;; query string and url-encoded form
    wrap-noir-validation
    wrap-noir-cookies
    wrap-noir-flash
    (wrap-noir-session {:store (memory-store mem),
                        :cookie-name "keepblog-app-session"})
))

(defn start [port]
  (ring/run-jetty app {:port port,
                       :join? false}))

(defn -main []
  (schema/migrate)
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (start port)))

