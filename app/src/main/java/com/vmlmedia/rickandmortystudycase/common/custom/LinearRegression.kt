package com.vmlmedia.rickandmortystudycase.common.custom

/**
 * This class performs linear regression and calculates the regression parameters.
 *
 * @param x the independent variable data points as a FloatArray.
 * @param y the dependent variable data points as a FloatArray.
 * @throws IllegalArgumentException if the lengths of the x and y arrays are not equal.
 */
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
            throw IllegalArgumentException("Array lengths are not equal")
        }

        // Calculate sums and means of x and y
        var sumx = 0.0
        var sumy = 0.0
        var sumx2 = 0.0
        x.forEach { xi ->
            sumx += xi
            sumx2 += xi * xi
        }
        y.forEach { yi ->
            sumy += yi
        }
        val xbar = sumx / n
        val ybar = sumy / n

        // Calculate sums of squares and cross products
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

        // Calculate regression sum of squares and residual sum of squares
        var rss = 0.0  // Residual sum of squares
        var ssr = 0.0  // Regression sum of squares
        for (i in x.indices) {
            val fit = beta * x[i] + alpha
            rss += (y[i] - fit) * (y[i] - fit)
            ssr += (fit - ybar) * (fit - ybar)
        }

        // Calculate statistical measures
        val degreesOfFreedom = n - 2
        r2 = ssr / yybar
        svar = rss / degreesOfFreedom
        svar1 = svar / xxbar
        svar0 = svar / n + xbar * xbar * svar1
    }

    /**
     * Returns the y-intercept of the regression line.
     * @return the y-intercept as a Float.
     */
    fun intercept(): Float = alpha.toFloat()

    /**
     * Returns the slope of the regression line.
     * @return the slope as a Float.
     */
    fun slope(): Float = beta.toFloat()

    /**
     * Returns the coefficient of determination (R^2) of the regression.
     * @return the R^2 value as a Double.
     */
    fun r2(): Double = r2

    override fun toString(): String =
        "Linear Regression Model: y = %.2f x + %.2f (R^2 = %.3f)".format(slope(), intercept(), r2())
}