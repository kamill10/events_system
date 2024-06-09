import { useLoadingScreenState } from "../context/LoadingScreenContext";

export const useLoadingScreen = () => {
  const { isLoggingIn, isFetching, setIsLoggingIn, setIsFetching } =
    useLoadingScreenState();

  return {
    isFetching,
    setIsFetching,
    isLoggingIn,
    setIsLoggingIn,
  };
};
