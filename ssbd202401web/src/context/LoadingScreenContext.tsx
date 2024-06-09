import { ReactNode, createContext, useContext, useState } from "react";

interface LoadingScreenState {
  isLoggingIn: boolean;
  setIsLoggingIn: (state: boolean) => void;
  isFetching: boolean;
  setIsFetching: (state: boolean) => void;
}

const LoadingScreenStateContext = createContext<LoadingScreenState | null>(
  null,
);

export const LoadingScreenStateContextProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [isLoggingIn, setIsLoggingIn] = useState<boolean>(false);
  const [isFetching, setIsFetching] = useState<boolean>(false);

  return (
    <LoadingScreenStateContext.Provider
      value={{
        isLoggingIn,
        setIsLoggingIn,
        isFetching,
        setIsFetching,
      }}
    >
      {children}
    </LoadingScreenStateContext.Provider>
  );
};

export const useLoadingScreenState = () => {
  const loadingScreenState = useContext(LoadingScreenStateContext);

  if (!loadingScreenState) {
    throw new Error("You forgot about LoadingScreenStateContextProvider");
  }

  return loadingScreenState;
};
