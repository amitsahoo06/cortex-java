import { createApi, FetchArgs } from "@reduxjs/toolkit/query/react";
import { platformAuthConfig } from "./platformConfig";

export type AgentRequest = {
  prompt: string;
};

export type AgentResponse = {
  response: string;
};

const agentApi = createApi({
  reducerPath: "agentApi",
  ...platformAuthConfig(),
  tagTypes: ["Agent"],
  endpoints: (builder) => ({
    executeTask: builder.mutation<AgentResponse, AgentRequest>({
      query: (config): FetchArgs => ({
        url: `/agent/execute`,
        method: "POST",
        body: config,
      }),
    }),
  }),
});

export const { useExecuteTaskMutation } = agentApi;

export default agentApi;