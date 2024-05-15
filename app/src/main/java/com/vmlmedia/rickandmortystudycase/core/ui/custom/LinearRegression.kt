package com.vmlmedia.rickandmortystudycase.core.ui.custom

class LinearRegression(x: FloatArray, y: FloatArray) {
    private val n: Int = x.size
    private val alpha: Double
    private val beta: Double
    private val r2: Double
    private val svar: Double
    private val svar0: Double
    private val svar1: Double

    init {
        if (x.size != y.size) {
            throw IllegalArgumentException("array lengths are not equal")
        }

        // first pass: calculate sums and means
        var sumx = 0.0
        var sumy = 0.0
        var sumx2 = 0.0
        for (xi in x) {
            sumx += xi
            sumx2 += xi * xi
        }
        for (yi in y) {
            sumy += yi
        }
        val xbar = sumx / n
        val ybar = sumy / n

        // second pass: compute summary statistics
        var xxbar = 0.0
        var yybar = 0.0
        var xybar = 0.0
        for (i in x.indices) {
            val xi = x[i]
            val yi = y[i]
            xxbar += (xi - xbar) * (xi - xbar)
            yybar += (yi - ybar) * (yi - ybar)
            xybar += (xi - xbar) * (yi - ybar)
        }
        beta = xybar / xxbar
        alpha = ybar - beta * xbar

        // more statistical analysis
        var rss = 0.0  // residual sum of squares
        var ssr = 0.0  // regression sum of squares
        for (i in x.indices) {
            val fit = beta * x[i] + alpha
            rss += (fit - y[i]) * (fit - y[i])
            ssr += (fit - ybar) * (fit - ybar)
        }

        val degreesOfFreedom = n - 2
        r2 = ssr / yybar
        svar = rss / degreesOfFreedom
        svar1 = svar / xxbar
        svar0 = svar / n + xbar * xbar * svar1
    }

    fun intercept(): Float = alpha.toFloat()
    fun slope(): Float = beta.toFloat()
    fun r2(): Double = r2
    fun interceptStdErr(): Double = Math.sqrt(svar0)
    fun slopeStdErr(): Double = Math.sqrt(svar1)

    fun predict(x: Double): Double = beta * x + alpha

    override fun toString(): String =
        "%.2f N + %.2f".format(slope(), intercept()) + "  (R^2 = %.3f)".format(r2())
}