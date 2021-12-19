/* eslint-disable */
import { useState, useEffect } from "react";
import axios from "axios";

export const useFetch = ({
  initialUri = "",
  initialForm = {},
  contentType = "application/json",
  initialMethod = "get",
}) => {
  const [uri, setUri] = useState(initialUri);
  const [form, setForm] = useState(initialForm);
  const [method, setMethod] = useState(initialMethod);
  const [isLoading, setIsLoading] = useState(false);
  const { CancelToken } = axios;
  const { token, cancel } = CancelToken.source();

  useEffect(() => {
    return () => cancel("component unmount");
  }, [uri, form]);

  const fetch = async (data = null, config = {}) => {
    //console.log("config headers: ", config.headers);
    setIsLoading(true);
    const response = await axios({
      method: config.method || method,
      url: config.uri || uri,
      data: data || form,
      contentType: config.contentType || contentType,
      headers: config?.headers,
      // token,
    });
    setIsLoading(false);
    return response;
  };

  return {
    uri,
    setUri,
    form,
    setForm,
    method,
    setMethod,
    fetch,
    isLoading,
  };
};
