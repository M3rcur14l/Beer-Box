package com.m3rc.beerbox.bus.state

import com.m3rc.beerbox.bus.annotation.ExecutionState

data class LoadingState(@ExecutionState val loadingState: Int)
