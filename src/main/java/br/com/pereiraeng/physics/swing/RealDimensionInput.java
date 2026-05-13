package br.com.pereiraeng.physics.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;

import br.com.pereiraeng.core.LocaleConfig;
import br.com.pereiraeng.physics.Grandeza;
import br.com.pereiraeng.physics.Unidade;
import br.com.pereiraeng.swing.Grade;
import br.com.pereiraeng.swing.input.Input;

/**
 * Classe do objeto gráfico que permite a entrada e edição de dimensões planas
 * (duas coordenadas, largura e altura)
 * 
 * @author Philipe PEREIRA
 *
 */
public class RealDimensionInput extends Grade implements Input<Point2D.Float>, ActionListener {
	private static final long serialVersionUID = 1L;

	private MedidaInput w, h;

	private static final int PRECISION = 4;

	/**
	 * Construtor do objeto gráfico
	 */
	public RealDimensionInput() {
		this(null);
	}

	/**
	 * Construtor do objeto gráfico que já inclui butões com tamanhos padrão
	 * 
	 * @param suggestions tabela de tamanhos sugeridos
	 */
	public RealDimensionInput(Map<String, Point2D.Float> suggestions) {
		Unidade.loadUnitSets(15);
		add(new JLabel(LocaleConfig.getString("width")), 0, 0, 1, 1);
		w = new MedidaInput(0., PRECISION, Grandeza.COMPRIMENTO);
		w.setUnit(3);
		add(w, 1, 0, 1, 1);
		add(new JLabel(LocaleConfig.getString("height")), 0, 1, 1, 1);
		h = new MedidaInput(0., PRECISION, Grandeza.COMPRIMENTO);
		h.setUnit(3);
		add(h, 1, 1, 1, 1);

		if (suggestions != null) {
			int i = 0;
			for (Entry<String, Float> e : suggestions.entrySet()) {
				JButton b = new JButton(e.getKey());
				Point2D.Float xy = e.getValue();
				b.setActionCommand(String.format(Locale.US, "%." + PRECISION + "g;%." + PRECISION + "g", xy.x, xy.y));
				b.addActionListener(this);
				add(b, 2, i++, 1, 1);
			}
		}
	}

	@Override
	public void set(Point2D.Float wh) {
		w.set(wh.x);
		h.set(wh.y);
	}

	@Override
	public Point2D.Float get() {
		return new Point2D.Float(w.get().floatValue(), h.get().floatValue());
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String[] xy = event.getActionCommand().split(";");
		w.set(Double.parseDouble(xy[0]));
		h.set(Double.parseDouble(xy[1]));
	}
}
