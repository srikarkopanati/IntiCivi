function ServiceCards() {
  const cards = [
    {
      icon: "📋",
      title: "Register Complaint",
      desc: "Report a civic issue in your area",
      color: "#1a3c6e",
      accent: "#FF9900",
    },
    {
      icon: "🔍",
      title: "Track Complaint",
      desc: "Check real-time complaint status",
      color: "#0f5c3a",
      accent: "#138808",
    },
    {
      icon: "📁",
      title: "My Complaints",
      desc: "View all your submitted issues",
      color: "#5a1a6e",
      accent: "#a855f7",
    },
    {
      icon: "🤝",
      title: "Help / Support",
      desc: "Contact the concerned department",
      color: "#6e2a1a",
      accent: "#ef4444",
    },
  ];

  return (
    <>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500&family=Playfair+Display:wght@600&display=swap');

        .sc-section {
          padding: 60px 24px;
          font-family: 'DM Sans', sans-serif;
        }

        .sc-heading-row {
          display: flex;
          align-items: baseline;
          gap: 16px;
          margin-bottom: 36px;
        }

        .sc-heading {
          font-family: 'Playfair Display', serif;
          font-size: 26px;
          font-weight: 600;
          color: #0a1628;
          margin: 0;
        }

        .sc-heading-line {
          flex: 1;
          height: 1px;
          background: linear-gradient(to right, #e2e8f0, transparent);
        }

        .sc-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
          gap: 20px;
        }

        .sc-card {
          position: relative;
          background: #ffffff;
          border: 1px solid #e8edf5;
          border-radius: 12px;
          padding: 28px 24px 24px;
          cursor: pointer;
          overflow: hidden;
          transition: transform 0.28s cubic-bezier(0.34, 1.56, 0.64, 1),
                      box-shadow 0.28s ease,
                      border-color 0.25s ease;
        }

        .sc-card::before {
          content: '';
          position: absolute;
          top: 0; left: 0; right: 0;
          height: 3px;
          background: var(--accent);
          transform: scaleX(0);
          transform-origin: left;
          transition: transform 0.3s ease;
          border-radius: 12px 12px 0 0;
        }

        .sc-card::after {
          content: '';
          position: absolute;
          inset: 0;
          background: radial-gradient(circle at 20% 80%, var(--bg-color) 0%, transparent 60%);
          opacity: 0;
          transition: opacity 0.3s ease;
          border-radius: 12px;
        }

        .sc-card:hover {
          transform: translateY(-6px) scale(1.02);
          box-shadow: 0 16px 40px rgba(0,0,0,0.10), 0 4px 12px rgba(0,0,0,0.06);
          border-color: var(--accent);
        }

        .sc-card:hover::before {
          transform: scaleX(1);
        }

        .sc-card:hover::after {
          opacity: 0.06;
        }

        .sc-card:hover .sc-icon-wrap {
          background: var(--accent);
          transform: rotate(-6deg) scale(1.1);
        }

        .sc-card:hover .sc-icon {
          filter: grayscale(1) brightness(10);
        }

        .sc-card:hover .sc-btn {
          background: var(--accent);
          color: #fff;
          border-color: var(--accent);
          padding-right: 20px;
        }

        .sc-card:hover .sc-arrow {
          opacity: 1;
          transform: translateX(0);
        }

        .sc-icon-wrap {
          width: 48px;
          height: 48px;
          border-radius: 10px;
          background: var(--bg-color);
          display: flex;
          align-items: center;
          justify-content: center;
          margin-bottom: 16px;
          transition: background 0.25s ease, transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
        }

        .sc-icon {
          font-size: 22px;
          transition: filter 0.25s ease;
        }

        .sc-title {
          font-size: 15px;
          font-weight: 500;
          color: #0a1628;
          margin: 0 0 6px;
        }

        .sc-desc {
          font-size: 13px;
          font-weight: 300;
          color: #64748b;
          margin: 0 0 20px;
          line-height: 1.5;
        }

        .sc-btn {
          display: inline-flex;
          align-items: center;
          gap: 6px;
          padding: 8px 16px;
          font-size: 13px;
          font-weight: 500;
          font-family: 'DM Sans', sans-serif;
          color: var(--accent);
          background: transparent;
          border: 1px solid var(--accent);
          border-radius: 6px;
          cursor: pointer;
          transition: background 0.25s ease, color 0.25s ease, padding 0.25s ease;
          position: relative;
          z-index: 1;
        }

        .sc-arrow {
          opacity: 0;
          transform: translateX(-4px);
          transition: opacity 0.25s ease, transform 0.25s ease;
          font-style: normal;
        }
      `}</style>

      <div className="sc-section">
        <div className="sc-heading-row">
          <h2 className="sc-heading">Citizen Services</h2>
          <div className="sc-heading-line" />
        </div>

        <div className="sc-grid">
          {cards.map((card) => (
            <div
              key={card.title}
              className="sc-card"
              style={{
                "--accent": card.accent,
                "--bg-color": card.color + "18",
              }}
            >
              <div className="sc-icon-wrap">
                <span className="sc-icon">{card.icon}</span>
              </div>
              <p className="sc-title">{card.title}</p>
              <p className="sc-desc">{card.desc}</p>
              <button className="sc-btn">
                Open <em className="sc-arrow">→</em>
              </button>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

export default ServiceCards;